package store.j3studios.plugin.tasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import store.j3studios.plugin.SCore;

public class ProtectionsTask {
    
    private SCore core;
    
    public ProtectionsTask (SCore core) {
        this.core = core;
    }
    
    private List<String> hourTax = new ArrayList<>();
    
    public void loadHours() {
        String hour;
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                hour = "0" + i;
            } else {
                hour = ""+i;
            }
            hourTax.add(hour + ":00:00");
        }
        
        this.startRunnable();
    }
    
    public void startRunnable() {
        SCore.get().getServer().getScheduler().scheduleAsyncRepeatingTask(SCore.get(), new Runnable() {
            @Override
            public void run() {
                if (hourTax.contains(getHour())) {
                     
                }
             }
        }, 0, 20L);
    }
    
    public String getHour() {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
        return format.format(now);
    }
    
}
