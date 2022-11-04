import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * RedFantasy
 */
public class RedFantasy {
    String[] monsters = new String[22];
    int[] monstersPoint = new int[22];

    int[] playerMonsters = new int[5];
    int[] playerMonstersPoint = new int[5];

    int[] cpuMonsters = new int[5];
    int[] cpuMonstersPoint = new int[5];

    ArrayList<Integer> p3count = new ArrayList<Integer>();
    ArrayList<Integer> p4count = new ArrayList<Integer>();

    int playerHp = 50;
    int cpuHp = 50;
    int playerBonusPoint = 0;
    int cpuBonusPoint = 0;

    Random rnd = new Random();

    // battle history
    int[] playerHistory = new int[100];
    int[] cpuHistory = new int[100];
    
    public RedFantasy() {
        initPlayerMonster();
        initCpuMonster();

        this.playerHistory[0] = this.playerHp;
        this.cpuHistory[0] = this.cpuHp;

        initPlayerHistory();
        initCpuHistory();
    } 

    public void initPlayerHistory(){
        IntStream.range(0,this.playerHistory.length)
            .forEach(i -> {
                this.playerHistory[i] = -9999;
        });
    }

    public void initCpuHistory(){
        IntStream.range(0, this.cpuHistory.length)
            .forEach(i -> {
                this.cpuHistory[i] = -9999;
        });
    }

    public void initPlayerMonster() {
        IntStream.range(0, this.playerMonsters.length)
            .forEach(i -> {
                this.playerMonsters[i] = -1;
        });
    }

    public void initCpuMonster(){
        IntStream.range(0, this.cpuMonsters.length)
            .forEach(i -> {
                this.cpuMonsters[i] = -1;
        });
    }

    public void startPhase() {
        setRandomPlayerMonster();
        setRandomCpuMonster();

        System.out.println("--------------------");
        System.out.print("Player Monsters List:");

        printMonsterList(this.playerMonsters);
        
        System.out.print("\nCPU Monsters List:");

        printMonsterList(this.cpuMonsters);

        System.out.println("\n--------------------");
        System.out.println("Battle!");

        int d1 = this.rnd.nextInt(6)+1; //1~6のサイコロを振る
        int d2 = this.rnd.nextInt(6)+1; //1~6のサイコロを振る

        System.out.println("Player's Dice'：" + d1);
        diceMonster(d1, playerMonsters);

        System.out.println("CPU's Dice'：" + d2);
        diceMonster(d2, cpuMonsters);

        System.out.println("--------------------");

        int p3 = this.playerBonusPoint;
        int p4 = this.cpuBonusPoint;
        
        System.out.print("Player Monster Pointの合計:");
        p3 += countBonusPonint(playerMonsters, p3count).stream().mapToInt(Integer::intValue).sum();
        System.out.println(p3);

        System.out.print("CPU Monster Pointの合計:");
        p4 += countBonusPonint(cpuMonsters, p4count).stream().mapToInt(Integer::intValue).sum();
        System.out.println(p4);

        System.out.println("--------------------");

        gameResult(p3, p4);
        
        System.out.println("--------------------");

        // 対戦結果の記録
        saveGameResult();
    }

    public void setRandomPlayerMonster() {
        // Draw player's monster card
        // playerMonsters.length -3 ~ playerMonsters.length までのランダムなint型の数値をp1に代入する
        int p1 = this.rnd.nextInt(this.playerMonsters.length - 2) + 3;
        System.out.println("Player Draw " + p1 + " monsters");

        IntStream.range(0, p1)
            .forEach(i -> {
                int m = this.rnd.nextInt(this.monsters.length);
                this.playerMonsters[i] = m;
                this.playerMonstersPoint[i] = this.monstersPoint[m];
        });
    }

    public void setRandomCpuMonster(){
        ////Draw cpu's monster card
        int p2 = this.rnd.nextInt(this.cpuMonsters.length -2 ) + 3;
        System.out.println("CPU Draw " + p2 + " monsters");

        IntStream.range(0, p2)
            .forEach(i -> {
                int m = this.rnd.nextInt(this.monsters.length);
                this.cpuMonsters[i] = m;
                this.cpuMonstersPoint[i] = this.monstersPoint[m];
        });
    }

    public void printMonsterList(int[] monstersArray){
        IntStream.of(monstersArray)
            .filter(s -> s != -1)
            .forEach(i -> {
                System.out.print(this.monsters[i] + " ");
        });
    }

    public void diceMonster(int dice, int[] monstersList){
        if(dice == 1){
            System.out.println("失敗！すべてのモンスターポイントが半分になる");

            IntStream.range(0, monstersList.length)
            .filter(s -> monstersList[s] != -1)
            .forEach(i -> {
                this.playerMonstersPoint[i] = this.playerMonstersPoint[i] / 2;
            });

        }else if(dice == 6){
            System.out.println("Critical！すべてのモンスターポイントが倍になる");

            IntStream.range(0, monstersList.length)
            .filter(s -> monstersList[s] != -1)
            .forEach(i -> {
                this.playerMonstersPoint[i] = this.playerMonstersPoint[i] * 2;
            });
        }else{
            this.playerBonusPoint = dice;
        }
    }

    public ArrayList<Integer> countBonusPonint(int[] monsters, ArrayList<Integer> pCount){
        IntStream.of(monsters)
            .filter(s -> s != -1)
            .forEach(i -> {pCount.add(i);});

        return pCount;
    }

    public void gameResult(int p3, int p4) {
        if(p3 > p4){
            System.out.println("Player Win!");
            this.cpuHp = this.cpuHp - (p3 - p4);
        }else if(p4 > p3){
            System.out.println("CPU Win!");
            this.playerHp = this.playerHp - (p4 - p3);
        }else if(p3 == p4){
            System.out.println("Draw!");
        }

        System.out.println("Player HP is " + this.playerHp);
        System.out.println("CPU HP is " + this.cpuHp);
    }

    public void saveGameResult(){
        IntStream.of(this.playerHistory)
            .filter(s -> s == -9999)
            .forEach(i -> {i = this.playerHp;});

        IntStream.of(this.cpuHistory)
            .filter(s -> s == -9999)
            .forEach(i -> {i = this.cpuHp;});
    }

    public int[] getPlayerHistory(){
        return this.playerHistory;
    }
    public int[] getCpuHistory(){
        return this.cpuHistory;
    }

    public int getPlayerHp(){
        return this.playerHp; 
    }

    public int getCpuHp(){
        return this.cpuHp;
    }

    public void setMonstersPoint(int[] tempMonstersPoint) {
        this.monstersPoint = tempMonstersPoint;
    }

    public void setMonsterZukan(String[] tempMonsters) {
        this.monsters = tempMonsters;
    }

}