/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mobile.ui.issue;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.ViewUtils;
import com.github.mobile.R.id;
import com.github.mobile.util.AvatarLoader;
import com.github.mobile.util.TypefaceUtils;
import com.viewpagerindicator.R.layout;

import org.eclipse.egit.github.core.SearchIssue;
import org.eclipse.egit.github.core.User;

/**
 * Adapter for a list of searched for issues
 */
public class SearchIssueListAdapter extends IssueListAdapter<SearchIssue> {

    private int numberPaintFlags;

    /**
     * @param inflater
     * @param elements
     * @param avatars
     */
    public SearchIssueListAdapter(LayoutInflater inflater,
            SearchIssue[] elements, AvatarLoader avatars) {
        super(layout.repo_issue_item, inflater, elements, avatars);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getNumber();
    }

    @Override
    protected int getNumber(SearchIssue issue) {
        return issue.getNumber();
    }

    @Override
    protected View initialize(View view) {
        view = super.initialize(view);

        numberPaintFlags = textView(view, id.tv_issue_number).getPaintFlags();
        TypefaceUtils.setOcticons(
                (TextView) view.findViewById(id.tv_pull_request_icon),
                (TextView) view.findViewById(id.tv_comment_icon));
        for (int i = 0; i < MAX_LABELS; i++)
            ViewUtils.setGone(view.findViewById(id.v_label0 + i), true);
        ViewUtils.setGone(view.findViewById(id.tv_pull_request_icon), true);
        return view;
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { id.tv_issue_number, id.tv_issue_title, id.iv_avatar,
                id.tv_issue_creation, id.tv_issue_comments };
    }

    @Override
    protected void update(int position, SearchIssue issue) {
        updateNumber(issue.getNumber(), issue.getState(), numberPaintFlags,
                id.tv_issue_number);

        String gravatarId = issue.getGravatarId();
        User user;
        if (!TextUtils.isEmpty(gravatarId))
            user = new User().setGravatarId(gravatarId);
        else
            user = null;
        avatars.bind(imageView(id.iv_avatar), user);

        setText(id.tv_issue_title, issue.getTitle());

        updateReporter(issue.getUser(), issue.getCreatedAt(),
                id.tv_issue_creation);
        setNumber(id.tv_issue_comments, issue.getComments());
    }
}
