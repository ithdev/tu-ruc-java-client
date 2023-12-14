package org.ith.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class DataTableResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer draw;
    private Long recordsTotal;
    private Long recordsFiltered;
    private List<T> data;

    public DataTableResponse() {
    }

    public DataTableResponse(Integer draw, Long recordsTotal, Long recordsFiltered, List<T> data) {
        this.draw = draw;
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.data = data;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataTableResponse{" +
                "draw=" + draw +
                ", recordsTotal=" + recordsTotal +
                ", recordsFiltered=" + recordsFiltered +
                ", data=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataTableResponse<?> that)) return false;

        if (!Objects.equals(draw, that.draw)) return false;
        if (!Objects.equals(recordsTotal, that.recordsTotal)) return false;
        if (!Objects.equals(recordsFiltered, that.recordsFiltered))
            return false;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = draw != null ? draw.hashCode() : 0;
        result = 31 * result + (recordsTotal != null ? recordsTotal.hashCode() : 0);
        result = 31 * result + (recordsFiltered != null ? recordsFiltered.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

}
