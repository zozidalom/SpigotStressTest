package plugin.zozidalom.spigotstresstest;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        long startTime = System.nanoTime();
        System.out.println("Starting CPU test...");
        for(int i = Integer.MIN_VALUE; i<Integer.MAX_VALUE;++i) {
            for(int j = 0; j < 10000; ++j) {
            }
        }
        System.out.println("CPU test 1/2 completed");
        ArrayList<Thread> temp = new ArrayList<>();
        for(int i = 0; i<1500;++i) {
            Thread t = new Thread(() -> {
                for(int j = 0; j< 50000; j+=j+10) {

                }
            });
            temp.add(t);
            t.start();
        }
        temp.forEach(i -> {
            try {
                i.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long time = System.nanoTime()-startTime;
        System.out.println("Finished CPU test");
        System.out.println("Time: "+time+"ns ("+Math.floorDiv(time, 10000000)+"s)");
    }
}
