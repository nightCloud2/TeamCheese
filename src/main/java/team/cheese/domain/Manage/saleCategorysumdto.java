package team.cheese.domain.Manage;

public class saleCategorysumdto {
    String name;
    String total_view_cnt;

    public String getTotal_view_cnt() {
        return total_view_cnt;
    }

    public void setTotal_view_cnt(String total_view_cnt) {
        this.total_view_cnt = total_view_cnt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "saleCategorysumdto{" +
                "name='" + name + '\'' +
                ", total_view_cnt='" + total_view_cnt + '\'' +
                '}';
    }
}
