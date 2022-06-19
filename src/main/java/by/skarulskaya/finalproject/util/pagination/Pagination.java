package by.skarulskaya.finalproject.util.pagination;

public class Pagination {
    public static int offset(int itemPerPage, int currentPage){
        return itemPerPage * (currentPage - 1);
    }
}
