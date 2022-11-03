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

        initHistory();  
    } 

    public void initHistory(){
        IntStream.range(0, this.playerHistory.length)
            .forEach(i -> {
                this.playerHistory[i] = -9999;
                this.cpuHistory[i] = -9999; 
        });
    }

    public void initPlayerMonster() {
        IntStream.of(this.playerMonsters)
            .forEach(i -> {
                this.playerMonsters[i] = -1;
            });
    }

    public void initCpuMonster(){
        IntStream.of(this.cpuMonsters)
            .forEach(i -> {
                this.cpuMonsters[i] = -1;
        });
    }

    public void startPhase() {
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

        ////Draw cpu's monster card
        int p2 = this.rnd.nextInt(this.cpuMonsters.length -2 ) + 3;
        System.out.println("CPU Draw " + p2 + " monsters");

        IntStream.range(0, p2)
            .forEach(i -> {
                int m = this.rnd.nextInt(this.monsters.length);
                this.cpuMonsters[i] = m;
                this.cpuMonstersPoint[i] = this.monstersPoint[m];
            });
        

        System.out.println("--------------------");
        System.out.print("Player Monsters List:");

        IntStream.of(this.playerMonsters)
            .filter(s -> s != -1)
            .forEach(i -> {
                System.out.print(this.monsters[i] + " ");
        });
        
        System.out.print("\nCPU Monsters List:");

        IntStream.of(this.cpuMonsters)
            .filter(s -> s != -1)
            .forEach(i -> {
                System.out.print(this.monsters[i] + " ");
        });

        System.out.println("\n--------------------");
        System.out.println("Battle!");
        int d1 = this.rnd.nextInt(6)+1; //1~6のサイコロを振る
        System.out.println("Player's Dice'：" + d1);
        if(d1 == 1){
            System.out.println("失敗！すべてのモンスターポイントが半分になる");

            IntStream.range(0, this.playerMonsters.length)
            .filter(s -> this.playerMonsters[s] != -1)
            .forEach(i -> {
                this.playerMonstersPoint[i] = this.playerMonstersPoint[i] / 2;
            });

        }else if(d1 == 6){
            System.out.println("Critical！すべてのモンスターポイントが倍になる");

            IntStream.range(0, this.playerMonsters.length)
            .filter(s -> this.playerMonsters[s] != -1)
            .forEach(i -> {
                this.playerMonstersPoint[i] = this.playerMonstersPoint[i] * 2;
            });
        }else{
            this.playerBonusPoint = d1;
        }
        int d2 = this.rnd.nextInt(6)+1; //1~6のサイコロを振る
        System.out.println("CPU's Dice'：" + d2);
        if(d2 == 1){
            System.out.println("失敗！すべてのモンスターポイントが半分になる");
            IntStream.range(0, this.cpuMonsters.length)
            .filter(s -> this.cpuMonsters[s] != -1)
            .forEach(i -> {
                this.cpuMonstersPoint[i] = this.cpuMonstersPoint[i] / 2;
            });
        }else if(d2 == 6){
            System.out.println("Critical！すべてのモンスターポイントが倍になる");
            IntStream.range(0, this.cpuMonsters.length)
            .filter(s -> this.cpuMonsters[s] != -1)
            .forEach(i -> {
                this.cpuMonstersPoint[i] = this.cpuMonstersPoint[i] * 2;
            });
        }else{
            this.cpuBonusPoint = d2;
        }

        System.out.println("--------------------");
        System.out.print("Player Monster Pointの合計:");
        int p3 = this.playerBonusPoint;
        
        IntStream.of(this.playerMonsters)
            .filter(s -> s != -1)
            .forEach(i -> {p3count.add(i);});
        
        p3 += p3count.stream().mapToInt(Integer::intValue).sum();
        System.out.println(p3);

        System.out.print("CPU Monster Pointの合計:");
        int p4 = this.cpuBonusPoint;
        IntStream.of(this.cpuMonsters)
            .filter(s -> s != -1)
            .forEach(i -> {p4count.add(i);});
        
        p4 += p4count.stream().mapToInt(Integer::intValue).sum();
        System.out.println(p4);
        System.out.println("--------------------");

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
        
        System.out.println("--------------------");
        // 対戦結果の記録
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