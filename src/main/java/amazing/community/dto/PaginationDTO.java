package amazing.community.dto;

import amazing.community.model.Question;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {

    private List<QuestionDTO> questions;
    private Boolean showPrevious;
    private Boolean showFirst;
    private Boolean showNext;
    private Boolean showEnd;
    private Integer page;
    private List<Integer> pages;
    private Integer totalPage;

    /**
     *
     * @param totalCount total number of records in database
     * @param page the current page, start from 1
     * @param size the number of records in each page
     */
    public void setPagination(Integer totalCount, Integer page, Integer size) {

        //total number of pages
        this.totalPage = (totalCount+size-1)/size;
        this.page = page;

        pages = new ArrayList<>();
        int width = 3;
        int start = Math.max(Math.min(page - width, totalPage-2*width), 1);
        for(int i = start;i<=start+width*2 && i<=totalPage;i++)
        {
            pages.add(i);
        }

        //是否展示上一页
        showPrevious = page != 1;

        //是否展示下一页
        showNext = !page.equals(totalPage);

        //是否展示第一页
        showFirst = !pages.contains(1);

        //是否展示最后一页
        showEnd = !pages.contains(totalPage);
    }
}
