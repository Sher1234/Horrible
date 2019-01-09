package info.horriblesubs.sher.api.nyaa.model;

import java.util.HashMap;
import java.util.Map;

public class ListRequest {

    private String ordering;
    private String category;
    private String sortBy;
    private String query;
    private String user;
    private int filter;
    private int page;

    public ListRequest() {

    }

    public ListRequest setQuery(String query) {
        this.query = query;
        return this;
    }

    public ListRequest setCategory(TorrentCategory torrentCategory) {
        this.category = torrentCategory.getId();
        return this;
    }

    public ListRequest setFilter(Filter filter) {
        this.filter = filter.getId();
        return this;
    }

    public ListRequest setUser(String user) {
        this.user = user;
        return this;
    }

    public ListRequest setPage(int page) {
        this.page = page != 0 ? page : 1;
        return this;
    }

    public ListRequest setOrdering(Ordering ordering) {
        this.ordering = ordering.getId();
        return this;
    }

    public ListRequest setSortedBy(Sort sortBy) {
        this.sortBy = sortBy.getId();
        return this;
    }

    public Map<String, String> getRequestMap() {
        Map<String, String> map = new HashMap<>();
        if (ordering != null && !ordering.isEmpty()) map.put("o", ordering);
        if (category != null && !category.isEmpty()) map.put("c", category);
        if (sortBy != null && !sortBy.isEmpty()) map.put("s", sortBy);
        if (query != null && !query.isEmpty()) map.put("q", query);
        if (user != null && !user.isEmpty()) map.put("u", user);
        if (page != 0) map.put("p", String.valueOf(page));
        map.put("f", String.valueOf(filter));
        return map;
    }

    public enum Filter {
        NONE(0), NO_REMAKES(1), TRUSTED_ONLY(2);
        private final int id;

        Filter(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public enum Ordering {
        ASCENDING("asc"), DESCENDING("desc");
        private final String id;

        Ordering(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public enum Sort {
        DOWNLOADS("downloads"),
        COMMENTS("comments"),
        LEECHERS("leechers"),
        SEEDERS("seeders"),
        SIZE("size"),
        DATE("id");
        private final String id;

        Sort(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}