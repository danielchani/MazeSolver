package main.java.mazegame.repository;

import main.java.mazegame.model.MazeDataModel;
import java.io.*;
import java.util.*;

public class MyDMFileImpl implements IDao<MazeDataModel> {
    private final File dataFile;
    public MyDMFileImpl(String path) throws IOException {
        dataFile = new File(path);
        if (!dataFile.exists()) {
            dataFile.createNewFile();
            saveMap(new HashMap<>());
        }
    }
    @SuppressWarnings("unchecked")
    private Map<String,MazeDataModel> loadMap() {
        if (!dataFile.exists() || dataFile.length()==0) return new HashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            return (Map<String,MazeDataModel>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
    private void saveMap(Map<String,MazeDataModel> m) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(m);
        }
    }
    @Override public void save(MazeDataModel model) throws Exception {
        var m=loadMap(); if(m.containsKey(model.getId())) throw new IllegalArgumentException("ID exists");
        m.put(model.getId(), model); saveMap(m);
    }
    @Override public MazeDataModel find(String id) throws Exception { return loadMap().get(id); }
    @Override public void update(MazeDataModel model) throws Exception {
        var m=loadMap(); if(!m.containsKey(model.getId())) throw new IllegalArgumentException("ID missing");
        m.put(model.getId(), model); saveMap(m);
    }
    @Override public void delete(String id) throws Exception { var m=loadMap(); m.remove(id); saveMap(m); }
    @Override public List<String> listIds() throws Exception { return new ArrayList<>(loadMap().keySet()); }
}
