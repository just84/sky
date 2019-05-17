package games.game.of.life;

import pojo.Pair;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Life {
    private static int size = 360;
    private static int roundCheckLength = 8;
    private static int oldestIndex = 0;
    private static List<int[][]> roundCheckData = new ArrayList<>();

    public static int[][] 旅行者枪(){
        int[][] gun = new int[][]{
                {0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,1,1,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0},
                {1,1,1,1,0,0,1,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,1,0,0,0,0,0},
                {1,1,1,0,1,1,0,0,0,0,1,1,1,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,1,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0,1,0,0,1,0,0,0,0,0,0,0,0,0,1,1},
                {0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0}
        };
        int[][] data = new int[size][size];
        for(int i=0;i<9;i++){
            for(int j=0;j<36;j++){
                data[size/4+i][size/4+j] = gun[i][j];
            }
        }
        return data;
    }
    
    public static int getSize(){return size;}
    
    public void startWithRandom() {
        int[][] data = new int[size][size];
        makeRandomData(data);

        startWithData(data);
    }

    private void makeRandomData(int[][] data) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                data[i][j] = Math.random() > 0.7 ? 1 : data[i][j];
            }
        }
    }

    public int[][] makeSpecial(int[][] data){
        for(int i=100;i<size-100;i++){
            data[size/2][i]=1;
            data[i][size/2]=1;
        }
        return data;
    }

    public void startWithData(int[][] data) {
        Show show = new Show(size);
        int[][] ndata = new int[size][size];
        initRoundCheck(roundCheckLength,data,ndata);
        do {
            show.paintData(data);
            nextData(data, ndata);

            if(roundCheck()){
                makeSpecial(ndata);
            }

            data = ndata;
            ndata = roundCheckData.get(getRoundIndex(oldestIndex,-1));

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (show.isActive());
    }

    private boolean roundCheck() {
        boolean same = false;
        int[][] newest = roundCheckData.get(getRoundIndex(oldestIndex,-1));
        for(int i =0;i<roundCheckLength-1;i++){
            if(isSame(newest, roundCheckData.get(getRoundIndex(oldestIndex,i)))){
                same = true;
            }
        }
        oldestIndex = getRoundIndex(oldestIndex ,1);

        return same;
    }

    private int getRoundIndex(int oldestIndex, int move) {
        int i= (oldestIndex+move)%roundCheckLength;
        return i < 0 ? i+roundCheckLength : i;
    }

    private void initRoundCheck(int roundCheckLength, int[][] data, int[][] ndata) {
        roundCheckData = new ArrayList<>(roundCheckLength);
        for(int i =0;i<roundCheckLength-2;i++){
            roundCheckData.add(new int[size][size]);
        }
        roundCheckData.add(roundCheckLength -2,data);
        roundCheckData.add(roundCheckLength -1,ndata);
        oldestIndex = 0;
    }

    private void addLine(int[][] ndata) {
        int l = (int)(size * Math.random());
        boolean x = Math.random()>0.5;
        for(int i=0;i<size;i++){
            if(x) {
                ndata[l][i] = 1;
            }else {
                ndata[i][l]=1;
            }
        }
    }

    private boolean isSame(int[][] pdata, int[][] ndata) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(pdata[i][j] != ndata[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    private void nextData(int[][] data, int[][] ndata) {
        for (int i = 0; i < size; i++) {
            for(int j=0;j<size;j++){
                ndata[i][j] = getNext(data,i,j);
            }
        }
    }

    private int getNext(int[][] data, int i, int j) {
        int aliveCount = 0;
        List<Pair<Integer,Integer>> pairs = new ArrayList<>();
        pairs.add(new Pair(i-1,j));
        pairs.add(new Pair(i-1,j-1));
        pairs.add(new Pair(i-1,j+1));
        pairs.add(new Pair(i,j-1));
        pairs.add(new Pair(i,j+1));
        pairs.add(new Pair(i+1,j-1));
        pairs.add(new Pair(i+1,j));
        pairs.add(new Pair(i+1,j+1));

        for(Pair<Integer,Integer> pair : pairs){
            if(!outOfWorld(pair.getFirst(),pair.getSecond())){
                aliveCount += data[pair.getFirst()][pair.getSecond()];
            }
        }

        switch (aliveCount){
            case 3:return 1;
            case 2:return data[i][j];
            default:return 0;
        }
    }

    private boolean outOfWorld(int i, int j) {
        return i<0||j<0||i>=size||j>=size;
    }
}
