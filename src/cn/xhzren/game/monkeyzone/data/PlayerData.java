package cn.xhzren.game.monkeyzone.data;

import java.util.*;

public class PlayerData {

    private static HashMap<Long, PlayerData> players = new HashMap<>();
    private long id;
    private int aiControl = -1;
    private static HashMap<String, Float> floatData = new HashMap<>();
    private static HashMap<String, Long> longData = new HashMap<>();
    private static HashMap<String, Integer> intData = new HashMap<>();
    private static HashMap<String, Boolean> booleanData = new HashMap<>();
    private static HashMap<String, String> stringData = new HashMap<>();

    /**
     * 获取人形生物的数据
     * @return
     */
    public static synchronized List<PlayerData> getHumanPlayers() {
        LinkedList<PlayerData> list = new LinkedList<>();
        for(Iterator<Map.Entry<Long, PlayerData>> it = players.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Long, PlayerData> entry = it.next();
            if(entry.getValue().isHuman()) {
                list.add(entry.getValue());
            }
        }
        return list;
    }

    /**
     * 获取具有AI生物的数据
     * @return
     */
    public static synchronized List<PlayerData> getAIPlayers() {
        LinkedList<PlayerData> list = new LinkedList<>();
        for(Iterator<Map.Entry<Long, PlayerData>> it = players.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Long, PlayerData> entry = it.next();
            if(!entry.getValue().isHuman()) {
                list.add(entry.getValue());
            }
        }
        return list;
    }

    /**
     * 获取全部生物的数据
     * @return
     */
    public static synchronized List<PlayerData> getPlayers() {
        LinkedList<PlayerData> list = new LinkedList<PlayerData>(players.values());
        return list;
    }

    /**
     * 得到一个新的生物
     * @param name
     * @return
     */
    public static synchronized long getNew(String name) {
        long id = 0;
        while(players.containsKey(id)) {
            id++;
        }
        players.put(id, new PlayerData(id, name));
        return id;
    }

    public static synchronized void add(long id, PlayerData player) {
        players.put(id, player);
    }

    public static synchronized void remove(long id) {
        players.remove(id);
    }

    public static synchronized int getAiControl(long id) {
        return players.get(id).getAiControl();
    }
    public static synchronized void setAiControl(long id, int aiControl) {
        players.get(id).setAiControl(aiControl);
    }
    public static synchronized boolean isHuman(long id) {
        return players.get(id).isHuman();
    }
    public static synchronized float getFloatData(long id, String key) {
        if (!players.containsKey(id)) return -1;
        return players.get(id).getFloatData(key);
    }
    public static synchronized void setData(long id, String key, float data) {
        if (!players.containsKey(id)) return;
        players.get(id).setData(key, data);
    }
    public static synchronized long getLongData(long id, String key) {
        if (!players.containsKey(id)) return -1;
        return players.get(id).getLongData(key);
    }
    public static synchronized void setData(long id, String key, long data) {
        if (!players.containsKey(id)) return;
        players.get(id).setData(key, data);
    }
    public static synchronized boolean getBooleanData(long id, String key) {
        if (!players.containsKey(id)) return false;
        return players.get(id).getBooleanData(key);
    }
    public static synchronized void setData(long id, String key, boolean data) {
        if (!players.containsKey(id)) return;
        players.get(id).setData(key, data);
    }
    public static synchronized int getIntData(long id, String key) {
        if (!players.containsKey(id)) return -1;
        return players.get(id).getIntData(key);
    }
    public static synchronized void setData(long id, String key, int data) {
        if (!players.containsKey(id)) return;
        players.get(id).setData(key, data);
    }
    public static synchronized String getStringData(long id, String key) {
        if (!players.containsKey(id)) return "unknown";
        return players.get(id).getStringData(key);
    }
    public static synchronized void setData(long id, String key, String data) {
        if (!players.containsKey(id)) return;
        players.get(id).setData(key, data);
    }

    public PlayerData(long id) {
        this.id = id;
    }

    public PlayerData(long id, String name) {
        this.id = id;
        setData("name", name);
        setData("entity_id", (long)-1);
    }
    public PlayerData(long id, int groupId, String name) {
       this(id, groupId, name, -1);
    }
    public PlayerData(long id, int groupId, String name, int aiControl) {
        this.id = id;
        this.aiControl = aiControl;
        setData("name", name);
        setData("entity_id", (long)-1);
        setData("group_id", groupId);
    }

    public void setData(String key, float data) {
        floatData.put(key, data);
    }
    public float getFloatData(String key) {
        return floatData.get(key);
    }
    public void setData(String key, long data) {
        longData.put(key, data);
    }
    public long getLongData(String key) {
        return longData.get(key);
    }
    public void setData(String key, boolean data) {
        booleanData.put(key, data);
    }
    public boolean getBooleanData(String key) {
        return booleanData.get(key);
    }
    public void setData(String key, Integer data) {
        intData.put(key, data);
    }
    public int getIntData(String key) {
        return intData.get(key);
    }
    public void setData(String key, String data) {
        stringData.put(key, data);
    }
    public String getStringData(String key) {
        return stringData.get(key);
    }

    public long getId() {
        return id;
    }

    public int getAiControl() {
        return aiControl;
    }

    public void setAiControl(int aiControl) {
        this.aiControl = aiControl;
    }

    public boolean isHuman() {
        return aiControl == -1;
    }

}
