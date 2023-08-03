package com.ssafy.fcc.util;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class PageNavigation {
	
	private int totalCnt; //총 게시글
	private int totalPageCnt; //전체 페이지 수 
	private boolean startRange; //이전 보일지
	private boolean endRange; //다음 보여줄 지
	private int naviSize; //네비게이션 사이즈
	private int pageSize; // 페이지당 글 갯수
	private int currentPage;
	
	private String navigator;

	
	public Map<String, Object> calPage(int pgno, int sizePerPage ){
		
		Map<String, Object> pageMap = new HashMap<String, Object>();
		
		int startPage = (pgno - 1) / naviSize * naviSize + 1;
		int endPage = startPage + (naviSize - 1);
		if (endPage > totalPageCnt)
			endPage = totalPageCnt;
		int pre = startRange ? 1 : (startPage - 1);
		int next = endRange ? endPage : (endPage + 1);
		int start = pgno * sizePerPage - sizePerPage;

		
		pageMap.put("startPage",startPage);
		pageMap.put("endPage",endPage);
		pageMap.put("pre",pre);
		pageMap.put("next",next);
		pageMap.put("start",start);
		
		return pageMap;
	}
	
	@Override
	public String toString() {
		return "PageNavigation [totalCnt=" + totalCnt + ", totalPageCnt=" + totalPageCnt + ", startRange=" + startRange
				+ ", endRange=" + endRange + ", naviSize=" + naviSize + ", pageSize=" + pageSize + ", currentPage="
				+ currentPage + "]";
	}
	
}
	
	