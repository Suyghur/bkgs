package com.pro.maluli.common.utils;

import com.netease.nim.uikit.business.contact.core.item.AbsContactItem;
import com.netease.nim.uikit.business.contact.core.item.ContactItem;
import com.netease.nim.uikit.business.contact.core.item.ContactItemFilter;
import com.netease.nim.uikit.business.contact.core.model.IContact;
import com.netease.nim.uikit.business.session.myCustom.base.DemoCache;

/**
 * @author sunkeding
 * 过滤掉自己
 */
public class ContactSelfFilter implements ContactItemFilter {
    private static final long serialVersionUID = -7818895495933185804L;
    @Override
    public boolean filter(AbsContactItem item) {
        IContact contact = ((ContactItem) item).getContact();
        // 过滤掉自己
        return contact.getContactId().equals(DemoCache.getAccount());
    }
}
