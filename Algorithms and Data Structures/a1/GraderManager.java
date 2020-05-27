import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.text.DecimalFormat;



public class GraderManager {
    static class Mark{

        DecimalFormat format = new DecimalFormat("##.00");

        double score;
        double outOf;
        public Mark(double score, double outOf) {
            this.score = score;
            this.outOf = outOf;
        }
        static final Mark zero = new Mark(0,-1);
        @Override
        public String toString() {
            return "Mark [" + score + " / " + outOf + "]";
        }

        public double getGrade(){
            return score;
        }

        public Mark combine(Mark other) {
            return new Mark(this.score + other.score, this.outOf + other.outOf);
        }


        private static DecimalFormat df = new DecimalFormat("0.00");

        public double normalizeScore(int max){
            double temp = (double) score/outOf * max;
            return Double.parseDouble(df.format(temp));
        }


    }
    ExecutorService pool = null;

    List<BiFunction<List<String>, Integer, Mark>> gradingPredicates = new ArrayList<>();

    // Grader stats
    boolean[] wasTimeout = null;
    List<String> messages = new ArrayList<>();
    List<Mark> grades = new ArrayList<>();

    // sub Graders
    List<GraderManager> sections = new ArrayList<>();

    Mark grade = new Mark(-1,-1);

    public void startPool() {
        this.pool = Executors.newFixedThreadPool(10);
    }

    public Mark grade() {
        if(pool==null || pool.isShutdown()) {
            startPool();
        }
        if(grade.score != -1) return grade;
        // Grade itself first
        wasTimeout = new boolean[gradingPredicates.size()];
        IntStream.range(0, this.gradingPredicates.size()).forEach(i->messages.add(""));

        List<CompletableFuture<Mark>> grades =IntStream.range(0, gradingPredicates.size())
                .boxed()
                .map(i->{
                    BiFunction<List<String>, Integer, Mark> biFunction = gradingPredicates.get(i);
                    return CompletableFuture.supplyAsync(()-> biFunction.apply(this.messages, i), pool);
                }).collect(Collectors.toList());

        this.grades = IntStream.range(0, grades.size())
                .boxed().parallel()
                .map(i->{
                    CompletableFuture<Mark> futureGrade = grades.get(i);
                    try {
                        return futureGrade.get(120, TimeUnit.SECONDS);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }  catch (TimeoutException e) {
                        wasTimeout[i] = true;
                    }
                    return Mark.zero;
                }).collect(Collectors.toList());

        pool.shutdown();
        this.grade = this.grades.stream().reduce(new Mark(0,0), (acc,x)-> acc.combine(x));
        return this.grade;
    }

    public Mark reweightedGrade(double maxScore) {
        Mark grade = this.grade();
        return new Mark( grade.score / grade.outOf * maxScore, maxScore);
    }

    public void addGrading(BiFunction<List<String>, Integer, Mark> grading) {
        gradingPredicates.add(grading);
    }

    public void displayGrades() {
        IntStream.range(0, this.grades.size()).forEach(i->{
            System.out.println("(timeout: "+ wasTimeout[i]+") "+this.messages.get(i)+ " grade: " + this.grades.get(i));
        });
    }

    //TODO expand to subsections
    public void addSubsection(GraderManager section) {
        this.sections.add(section);
    }
}
