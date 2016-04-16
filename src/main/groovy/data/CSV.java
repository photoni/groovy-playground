package data;

import java.util.*;

/**
 * Created by filippo on 16.04.16.
 */
public class CSV {
    private String[] header;
    private String[] unique;
    private Set<Integer> uniqueHashCodes=new HashSet<>();
    private List<String[]> lines=new LinkedList();

    public CSV(List<String[]> lines) {
        this.header=lines.get(0);
        this.lines = lines.subList(1,lines.size()-1);
    }

    public String[] getHeader() {
        return header;
    }

    public void setHeader(String[] header,String[] unique) {
        this.header = header;
        this.unique= unique;
    }
    public void setHeader(String[] header) {
        this.header = header;
    }

    public List<String[]> getLines() {
        return lines;
    }

    public void setLines(List<String[]> lines) {
        this.lines = lines;
    }
    public void addLine(String[] line) {
        int hash = Arrays.hashCode(line);
        if (!uniqueHashCodes.contains(hash)){
            this.lines.add(line);
            uniqueHashCodes.add(hash);
        }

    }
    public void addLines(List<String[]> lines) {
        this.lines.addAll(lines);
    }
    public List<String[]> getCsv() {
        List<String[]> csv=new LinkedList<>();
        csv.add(this.header);
        csv.addAll(this.lines);
        return csv;
    }
}
