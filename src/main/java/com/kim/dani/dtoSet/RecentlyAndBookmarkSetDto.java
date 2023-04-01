package com.kim.dani.dtoSet;


import com.kim.dani.entity.Member;
import com.kim.dani.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecentlyAndBookmarkSetDto {

    private RecentlySet[] recentlySets = new RecentlySet[5];

    private List<BookmarkSet> bookmarkSets = new ArrayList<>();


}
