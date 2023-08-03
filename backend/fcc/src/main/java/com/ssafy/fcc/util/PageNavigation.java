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
	
	private int pgno; // 현재 페이지
	private Long totalCnt; //총 게시글

	private int totalPageCnt; //전체 페이지 수
	private boolean startRange;
	private boolean endRange;

	private final int naviSize=5;
	private final int sizePerPage=10; // 페이지당 글 갯수

	private int startPage;
	private int endPage;
	private int pre;
	private int next;

	private int start;

	public PageNavigation(int pgno, Long totalCnt){
		this.pgno = pgno;
		this.totalCnt= totalCnt;

		totalPageCnt = (int)((totalCnt - 1) / sizePerPage + 1);
		startRange = pgno <= naviSize;
		endRange = (totalPageCnt - 1) / naviSize * naviSize < pgno;

		 startPage = (pgno - 1) / naviSize * naviSize + 1;
		 endPage = startPage + (naviSize - 1);
		if (endPage > totalPageCnt)
			endPage = totalPageCnt;
		 pre = startRange ? 1 : (startPage - 1);
		 next = endRange ? endPage : (endPage + 1);
		 start = pgno * sizePerPage - sizePerPage;
	}

	
}
	
	