import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;


public class Grader {


    public static void main(String[] args) {

        int maxScore = 40;

        System.out.println("Test open addressing probe");
        GraderManager probeGrade = new GraderManager();
        Open_Addressing addressing = new Open_Addressing(10, 0, -1);

        probeGrade.addGrading((messages, i)-> {
            messages.set(i, "Testing default case");
            return addressing.probe(1, 0)==30? new GraderManager.Mark(1, 1) : new GraderManager.Mark(0, 1);
        });
        probeGrade.addGrading((messages, i)-> {
            messages.set(i, "Testing i");
            return addressing.probe(1, 1)==31? new GraderManager.Mark(1, 1) : new GraderManager.Mark(0, 1);
        });
        probeGrade.addGrading((messages, i)-> {
            messages.set(i, "Testing i modulo");
            return addressing.probe(1, 3)==1? new GraderManager.Mark(1, 1) : new GraderManager.Mark(0, 1);
        });
        probeGrade.addGrading((messages, i)-> {
            messages.set(i, "Testing different key 1");
            return addressing.probe(2, 0)==28? new GraderManager.Mark(1, 1) : new GraderManager.Mark(0, 1);
        });
        probeGrade.addGrading((messages, i)-> {
            messages.set(i, "Testing different key 2");
            return addressing.probe(4, 0)==25? new GraderManager.Mark(1, 1) : new GraderManager.Mark(0, 1);
        });
//        probeGrade.addGrading((messages, i)-> {
//            messages.set(i, "Testing overflow ");
//            return addressing.probe(Integer.MAX_VALUE, 0)==1? new GraderManager.Mark(1, 1) : new GraderManager.Mark(0, 1);
//        });
        GraderManager.Mark probeTotal = probeGrade.grade();
        probeGrade.displayGrades();
        System.out.println("Probe "+probeGrade.grade);

        System.out.println("Test chaining probe");
        GraderManager chainGrade = new GraderManager();
        Chaining chaining = new Chaining(10, 0, -1);
        chainGrade.addGrading((messages, i)-> {
            messages.set(i, "Testing default case "+chaining.chain(1));
            return chaining.chain(1)==30? new GraderManager.Mark(1, 1) : new GraderManager.Mark(0, 1);
        });
//        chainGrade.addGrading((messages, i)-> {
//            messages.set(i, "Testing overflow case "+chaining.chain(Integer.MAX_VALUE));
//            return chaining.chain(Integer.MAX_VALUE)==1? new GraderManager.Mark(1, 1) : new GraderManager.Mark(0, 1);
//        });
        chainGrade.addGrading((messages, i)-> {
            messages.set(i, "Testing different key ");
            return chaining.chain(4)==25? new GraderManager.Mark(1, 1) : new GraderManager.Mark(0, 1);
        });
        chainGrade.addGrading((messages, i)-> {
            messages.set(i, "Testing different key ");
            return chaining.chain(8)==19? new GraderManager.Mark(1, 1) : new GraderManager.Mark(0, 1);
        });
        chainGrade.addGrading((messages, i)-> {
            messages.set(i, "Testing different key ");
            return chaining.chain(16)==6? new GraderManager.Mark(1, 1) : new GraderManager.Mark(0, 1);
        });
        chainGrade.grade();
        chainGrade.displayGrades();
        System.out.println("Chain "+chainGrade.grade);

        System.out.println("Test insert key for chaining");
        GraderManager chainingInsertGrade = new GraderManager();
        Chaining chainInsert = new Chaining(10, 0, -1);

        chainingInsertGrade.addGrading((messages, i)->{
            messages.set(i, "Testing insert key into arraylist");
            int initsize = chaining.Table.get(chaining.chain(0)).size();
            chainInsert.insertKey(0);
            return chainInsert.Table.get(chaining.chain(0)).size()-initsize == 1? new GraderManager.Mark(1,1) : new GraderManager.Mark(0, 1);
        });
        chainingInsertGrade.addGrading((messages, i)->{
            messages.set(i, "Testing collisions");
            chainInsert.insertKey(32);
            chainInsert.insertKey(52);
            int collisions = chainInsert.insertKey(72);
            return  collisions == 2 ? new GraderManager.Mark(1,1) : new GraderManager.Mark(0, 1);
        });

        chainingInsertGrade.grade();
        chainingInsertGrade.displayGrades();
        System.out.println("Chain insert "+chainingInsertGrade.grade);

        System.out.println("Test insert key for open addressing");
        GraderManager openInsertGrade = new GraderManager();
        Open_Addressing openInsert = new Open_Addressing(10, 0, -1);
        openInsertGrade.addGrading((messages, i)->{
            messages.set(i, "Testing insert key ");
            openInsert.insertKey(0);
            return openInsert.Table[openInsert.probe(0, 0)] == 0? new GraderManager.Mark(1,1): new GraderManager.Mark(0, 1);
        });
        Open_Addressing openInsertFull = new Open_Addressing(10, 0, -1);
        openInsertGrade.addGrading((messages, i)->{
            messages.set(i, "Testing insert key when full ");
            IntStream.range(0, openInsertFull.Table.length).forEach(n->openInsertFull.insertKey(n));
            try {
                openInsertFull.insertKey(33);
            }

            catch(Exception e){
                return new GraderManager.Mark(0, 1);
            }
            return Arrays.stream(openInsertFull.Table)
                    .mapToObj(n->n!=33)
                    .reduce((acc,x)->acc&&x).get() ? new GraderManager.Mark(1,1): new GraderManager.Mark(0, 1);
        });
        openInsertGrade.addGrading((messages, i)->{
            messages.set(i, "Testing collisions ");
            openInsert.insertKey(32);
            openInsert.insertKey(52);
            openInsert.insertKey(72);
            int collisions = openInsert.insertKey(92);
            return collisions==3? new GraderManager.Mark(1,1): new GraderManager.Mark(0, 1);
        });


        openInsertGrade.grade();
        openInsertGrade.displayGrades();
        System.out.println("Open insert "+openInsertGrade.grade);


        System.out.println("Test remove key for open addressing");
        GraderManager openRemoveGrade = new GraderManager();
        Open_Addressing openRemove = new Open_Addressing(10, 0, -1);
        openRemoveGrade.addGrading((messages,i)->{
            messages.set(i, "Testing remove key default case");
            openInsert.removeKey(0);
            return openInsert.Table[0]!=0 ? new GraderManager.Mark(1, 1): new GraderManager.Mark(0, 1);
        });

        openRemoveGrade.addGrading((messages,i)->{
            messages.set(i, "Testing remove key more than 1 collided case");
            openInsert.removeKey(52);
            return openInsert.Table[14]!=52 ? new GraderManager.Mark(1, 1): new GraderManager.Mark(0, 1);
        });

        openRemoveGrade.addGrading((messages,i)->{
            messages.set(i, "Testing removing key with more than 1 collided then adding a key back");
            openRemove.insertKey(32);
            openRemove.insertKey(52);
            openRemove.insertKey(72);
            openRemove.removeKey(52);
            openRemove.insertKey(92);
            return openRemove.Table[13]==32 &&
                    openRemove.Table[14]==92 &&
                    openRemove.Table[15]==72? new GraderManager.Mark(1, 1): new GraderManager.Mark(0, 1);
        });
        openRemoveGrade.addGrading((messages,i)->{
            messages.set(i, "Testing collisions");
            openInsert.insertKey(69);
            openInsert.insertKey(89);
            openInsert.insertKey(109);
            openInsert.insertKey(129);
            int collisions = openInsert.removeKey(109);
            return collisions == 2 ? new GraderManager.Mark(1, 1): new GraderManager.Mark(0, 1);
        });

        openRemoveGrade.grade();
        openRemoveGrade.displayGrades();
        System.out.println("Open remove "+openRemoveGrade.grade);


        GraderManager openRemoveInvalidKeyGrade = new GraderManager();
        Open_Addressing openRemoveInvalidKey = new Open_Addressing(10, 0, -1);
        openRemoveInvalidKeyGrade.addGrading((messages,i)->{
            messages.set(i, "Testing collisions if key is not in HashTable");
            openRemoveInvalidKey.insertKey(32);
            openRemoveInvalidKey.insertKey(52);
            openRemoveInvalidKey.insertKey(72);
            openRemoveInvalidKey.insertKey(92);
            int collisions = openRemoveInvalidKey.removeKey(1);
            return collisions == 0 ? new GraderManager.Mark(1, 1): new GraderManager.Mark(0, 1);
        });

        openRemoveInvalidKeyGrade.grade();
        openRemoveInvalidKeyGrade.displayGrades();
        System.out.println("Open remove "+openRemoveInvalidKeyGrade.grade);


//        System.out.println("Total "+ (probeGrade.reweightedGrade(10).combine(
//                chainGrade.reweightedGrade(10).combine(
//                        chainingInsertGrade.reweightedGrade(5).combine(
//                                openInsertGrade.reweightedGrade(5).combine(
//                                        openRemoveGrade.reweightedGrade(5).combine(
//                                                openRemoveInvalidKeyGrade.reweightedGrade(5))))))).getGrade());

        double finalGrade = (probeGrade.reweightedGrade(10).combine(
                chainGrade.reweightedGrade(10).combine(
                        chainingInsertGrade.reweightedGrade(5).combine(
                                openInsertGrade.reweightedGrade(5).combine(
                                        openRemoveGrade.reweightedGrade(5).combine(
                                                openRemoveInvalidKeyGrade.reweightedGrade(5))))))).getGrade();

        System.out.println("Final Grade: " + finalGrade);

        System.out.println("Done");
    }
}
