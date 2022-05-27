package com.netease.nim.uikit.business.team.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.dialog.MenuDialog;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.widget.SwitchButton;
import com.netease.nim.uikit.common.util.collection.CollectionUtil;
import com.netease.nim.uikit.common.util.log.sdk.wrapper.NimLog;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.TeamServiceObserver;
import com.netease.nimlib.sdk.team.constant.TeamMemberType;
import com.netease.nimlib.sdk.team.model.TeamMember;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 群成员详细信息界面
 * Created by hzxuwen on 2015/3/19.
 */
public class AdvancedTeamMemberInfoActivity extends UI implements View.OnClickListener {

    // constant
    public static final int REQ_CODE_REMOVE_MEMBER = 11;
    public static final String EXTRA_ISADMIN = "EXTRA_ISADMIN";
    public static final String EXTRA_ISREMOVE = "EXTRA_ISREMOVE";
    private static final String TAG = AdvancedTeamMemberInfoActivity.class.getSimpleName();
    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String EXTRA_TID = "EXTRA_TID";
    private final String KEY_MUTE_MSG = "mute_msg";

    // data
    private String account;

    private String teamId;

    private TeamMember viewMember;

    private boolean isSetAdmin;

    // view
    private HeadImageView headImageView;

    private TextView memberName;

    private TextView nickName;

    private TextView identity;

    private View nickContainer;

    private Button removeBtn;

    private View identityContainer;

    private MenuDialog setAdminDialog;

    private MenuDialog cancelAdminDialog;

    private View isMuteContainer;

    private TextView setMuteTv;

    private SwitchButton muteSwitch;
    private final SwitchButton.OnChangedListener onChangedListener = new SwitchButton.OnChangedListener() {

        @Override
        public void OnChanged(View v, final boolean checkState) {
            final String key = (String) v.getTag();
            if (!NetworkUtil.isNetAvailable(AdvancedTeamMemberInfoActivity.this)) {
                ToastHelper.showToast(AdvancedTeamMemberInfoActivity.this, R.string.network_is_not_available);
                if (key.equals(KEY_MUTE_MSG)) {
                    muteSwitch.setCheck(!checkState);
                }
                return;
            }
            if (key.equals(KEY_MUTE_MSG)) {
                NIMClient.getService(TeamService.class).muteTeamMember(teamId, account, checkState).setCallback(
                        new RequestCallback<Void>() {

                            @Override
                            public void onSuccess(Void param) {
                                if (checkState) {
                                    ToastHelper.showToast(AdvancedTeamMemberInfoActivity.this, "群禁言成功");
                                } else {
                                    ToastHelper.showToast(AdvancedTeamMemberInfoActivity.this, "取消群禁言成功");
                                }
                            }

                            @Override
                            public void onFailed(int code) {
                                if (code == 408) {
                                    ToastHelper.showToast(AdvancedTeamMemberInfoActivity.this,
                                            R.string.network_is_not_available);
                                } else {
                                    ToastHelper.showToast(AdvancedTeamMemberInfoActivity.this, "on failed:" + code);
                                }
                                muteSwitch.setCheck(!checkState);
                            }

                            @Override
                            public void onException(Throwable exception) {
                            }
                        });
            }
        }
    };
    private TextView invitorInfo;
    // state
    private boolean isSelfCreator = false;
    private boolean isSelfManager = false;

    public static void startActivityForResult(Activity activity, String account, String tid) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID, account);
        intent.putExtra(EXTRA_TID, tid);
        intent.setClass(activity, AdvancedTeamMemberInfoActivity.class);
        activity.startActivityForResult(intent, REQ_CODE_REMOVE_MEMBER);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_advanced_team_member_info_layout);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.team_member_info;
        setToolBar(R.id.toolbar, options);
        parseIntentData();
        findViews();
        registerObservers(true);
        loadMemberInfo();
        initMemberInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        if (setAdminDialog != null) {
            setAdminDialog.dismiss();
        }
        if (cancelAdminDialog != null) {
            cancelAdminDialog.dismiss();
        }
    }

    private void registerObservers(boolean register) {
        NIMClient.getService(TeamServiceObserver.class).observeMemberUpdate(new Observer<List<TeamMember>>() {
            @Override
            public void onEvent(List<TeamMember> teamMembers) {
                if (TextUtils.isEmpty(teamId) || TextUtils.isEmpty(account)) {
                    NimLog.i(TAG, String.format("cancel handle team members update, can not find current info. teamId: %s, account: %s", teamId, account));
                    return;
                }
                // 当前页面的对应用户
                TeamMember pageMember = CollectionUtil.firstElement(teamMembers, teamMember -> {
                    return teamMember != null && account.equals(teamMember.getAccount()) && teamId.equals(teamMember.getTid());
                });
                if (pageMember != null) {
                    viewMember = pageMember;
                }
                // 当前登录的用户
                TeamMember currentUser = CollectionUtil.firstElement(teamMembers, teamMember -> {
                    return teamMember != null && NimUIKit.getAccount().equals(teamMember.getAccount()) && teamId.equals(teamMember.getTid());
                });
                if (pageMember != null || currentUser != null) {
                    updateMemberInfo();
                }
            }
        }, register);
    }

    private void parseIntentData() {
        account = getIntent().getStringExtra(EXTRA_ID);
        teamId = getIntent().getStringExtra(EXTRA_TID);
    }

    private void findViews() {
        nickContainer = findViewById(R.id.nickname_container);
        identityContainer = findViewById(R.id.identity_container);
        headImageView = findViewById(R.id.team_member_head_view);
        memberName = findViewById(R.id.team_member_name);
        nickName = findViewById(R.id.team_nickname_detail);
        identity = findViewById(R.id.team_member_identity_detail);
        removeBtn = findViewById(R.id.team_remove_member);
        invitorInfo = findViewById(R.id.invitor_info);
        isMuteContainer = findViewById(R.id.include_is_mute_toggle);
        setMuteTv = findViewById(R.id.user_profile_title);
        muteSwitch = findViewById(R.id.user_profile_toggle);

        setMuteTv.setText(R.string.mute_msg);
        muteSwitch.setOnChangedListener(onChangedListener);
        muteSwitch.setTag(KEY_MUTE_MSG);
        if (canHandleThisMember()) {
            boolean isMute = NimUIKit.getTeamProvider().getTeamMember(teamId, account).isMute();
            showIsMute(isMute);
            Log.i(TAG, "mute=" + isMute);
        }
        setClickListener();
    }

    private void setClickListener() {
        nickContainer.setOnClickListener(this);
        identityContainer.setOnClickListener(this);
        removeBtn.setOnClickListener(this);
    }

    private void showIsMute(boolean isMute) {
        muteSwitch.setCheck(isMute);
        isMuteContainer.setVisibility(View.VISIBLE);
    }

    private void hideIsMute() {
        isMuteContainer.setVisibility(View.GONE);
    }

    private void loadMemberInfo() {
        viewMember = NimUIKit.getTeamProvider().getTeamMember(teamId, account);
        if (viewMember != null) {
            updateMemberInfo();
        } else {
            requestMemberInfo();
        }
    }

    /**
     * 查询群成员的信息
     */
    private void requestMemberInfo() {
        NimUIKit.getTeamProvider().fetchTeamMember(teamId, account, new SimpleCallback<TeamMember>() {

            @Override
            public void onResult(boolean success, TeamMember member, int code) {
                if (success && member != null) {
                    viewMember = member;
                    updateMemberInfo();
                }
            }
        });
    }

    private void initMemberInfo() {
        memberName.setText(UserInfoHelper.getUserDisplayName(account));
        headImageView.loadBuddyAvatar(account);
    }

    private void updateMemberInfo() {
        updateMemberIdentity();
        updateMemberNickname();
        updateMemberInvitor();
        updateSelfIndentity();
        if (canHandleThisMember()) {
            removeBtn.setVisibility(View.VISIBLE);
            muteSwitch.setCheck(viewMember.isMute());
            isMuteContainer.setVisibility(View.VISIBLE);
        } else {
            removeBtn.setVisibility(View.GONE);
            isMuteContainer.setVisibility(View.GONE);
        }
    }

    private void updateMemberInvitor() {
        if (viewMember.getInvitorAccid() == null) {
            List<String> accids = new ArrayList<>();
            accids.add(viewMember.getAccount());
            NIMClient.getService(TeamService.class).getMemberInvitor(viewMember.getTid(), accids).setCallback(
                    new RequestCallbackWrapper<Map<String, String>>() {

                        @Override
                        public void onResult(int code, Map<String, String> result, Throwable exception) {
                            if (code == ResponseCode.RES_SUCCESS && result != null) {
                                String invitor = result.get(viewMember.getAccount());
                                if (invitor != null) {
                                    invitorInfo.setText(invitor);
                                }
                            }
                        }
                    });
        } else {
            invitorInfo.setText(viewMember.getInvitorAccid());
        }
    }

    /**
     * 更新群成员的身份
     */
    private void updateMemberIdentity() {
        if (viewMember.getType() == TeamMemberType.Manager) {
            identity.setText(R.string.team_admin);
            isSetAdmin = true;
        } else {
            isSetAdmin = false;
            if (viewMember.getType() == TeamMemberType.Owner) {
                identity.setText(R.string.team_creator);
            } else {
                identity.setText(R.string.team_member);
            }
        }
    }

    /**
     * 更新成员群昵称
     */
    private void updateMemberNickname() {
        nickName.setText(
                viewMember.getTeamNick() != null ? viewMember.getTeamNick() : getString(R.string.team_nickname_none));
    }

    /**
     * 获得用户自己的身份
     */
    private void updateSelfIndentity() {
        TeamMember selfTeamMember = NimUIKit.getTeamProvider().getTeamMember(teamId, NimUIKit.getAccount());
        updateSelfIndentity(selfTeamMember);
    }

    private void updateSelfIndentity(TeamMember selfTeamMember) {
        isSelfCreator = false;
        isSelfManager = false;
        if (selfTeamMember == null) {
            return;
        }
        if (selfTeamMember.getType() == TeamMemberType.Manager) {
            isSelfManager = true;
        } else if (selfTeamMember.getType() == TeamMemberType.Owner) {
            isSelfCreator = true;
        }
    }

    // 判断是否有权限
    private boolean canHandleThisMember() {
        if (isSelfCreator && !isSelf(account)) {
            return true;
        }
        if (isSelfManager && viewMember.getType() == TeamMemberType.Normal) {
            return true;
        }
        return false;
    }

    /**
     * 更新群昵称
     *
     * @param name
     */
    private void setNickname(final String name) {
        DialogMaker.showProgressDialog(this, getString(R.string.empty), true);
        NIMClient.getService(TeamService.class).updateMemberNick(teamId, account, name).setCallback(
                new RequestCallback<Void>() {

                    @Override
                    public void onSuccess(Void param) {
                        DialogMaker.dismissProgressDialog();
                        nickName.setText(name != null ? name : getString(R.string.team_nickname_none));
                        ToastHelper.showToast(AdvancedTeamMemberInfoActivity.this, R.string.update_success);
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                        ToastHelper.showToast(AdvancedTeamMemberInfoActivity.this,
                                String.format(getString(R.string.update_failed), code));
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.nickname_container) {
            editNickname();

        } else if (i == R.id.identity_container) {
            showManagerButton();

        } else if (i == R.id.team_remove_member) {
            showConfirmButton();

        } else {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AdvancedTeamNicknameActivity.REQ_CODE_TEAM_NAME && resultCode == Activity.RESULT_OK) {
            final String teamName = data.getStringExtra(AdvancedTeamNicknameActivity.EXTRA_NAME);
            setNickname(teamName);
        }
    }

    /**
     * 设置群昵称
     */
    private void editNickname() {
        if (isSelfCreator || isSelf(account)) {
            AdvancedTeamNicknameActivity.start(AdvancedTeamMemberInfoActivity.this, nickName.getText().toString());
        } else if (isSelfManager && identity.getText().toString().equals(getString(R.string.team_member))) {
            AdvancedTeamNicknameActivity.start(AdvancedTeamMemberInfoActivity.this, nickName.getText().toString());
        } else {
            ToastHelper.showToast(this, R.string.no_permission);
        }
    }


    /**
     * 显示设置管理员按钮
     */
    private void showManagerButton() {
        if (identity.getText().toString().equals(getString(R.string.team_creator))) {
            return;
        }
        if (!isSelfCreator) {
            return;
        }
        if (identity.getText().toString().equals(getString(R.string.team_member))) {
            switchManagerButton(true);
        } else {
            switchManagerButton(false);
        }
    }

    /**
     * 转换设置或取消管理员按钮
     *
     * @param isSet 是否设置
     */
    private void switchManagerButton(boolean isSet) {
        if (isSet) {
            if (setAdminDialog == null) {
                List<String> btnNames = new ArrayList<>();
                btnNames.add(getString(R.string.set_team_admin));
                setAdminDialog = new MenuDialog(this, btnNames, new MenuDialog.MenuDialogOnButtonClickListener() {

                    @Override
                    public void onButtonClick(String name) {
                        addManagers();
                        setAdminDialog.dismiss();
                    }
                });
            }
            setAdminDialog.show();
        } else {
            if (cancelAdminDialog == null) {
                List<String> btnNames = new ArrayList<>();
                btnNames.add(getString(R.string.cancel_team_admin));
                cancelAdminDialog = new MenuDialog(this, btnNames, new MenuDialog.MenuDialogOnButtonClickListener() {

                    @Override
                    public void onButtonClick(String name) {
                        removeManagers();
                        cancelAdminDialog.dismiss();
                    }
                });
            }
            cancelAdminDialog.show();
        }
    }

    /**
     * 添加管理员权限
     */
    private void addManagers() {
        DialogMaker.showProgressDialog(this, getString(R.string.empty));
        ArrayList<String> accountList = new ArrayList<>();
        accountList.add(account);
        NIMClient.getService(TeamService.class).addManagers(teamId, accountList).setCallback(
                new RequestCallback<List<TeamMember>>() {

                    @Override
                    public void onSuccess(List<TeamMember> managers) {
                        DialogMaker.dismissProgressDialog();
                        identity.setText(R.string.team_admin);
                        ToastHelper.showToastLong(AdvancedTeamMemberInfoActivity.this, R.string.update_success);
                        viewMember = managers.get(0);
                        updateMemberInfo();
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                        ToastHelper.showToastLong(AdvancedTeamMemberInfoActivity.this,
                                String.format(getString(R.string.update_failed), code));
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                });
    }

    /**
     * 撤销管理员权限
     */
    private void removeManagers() {
        DialogMaker.showProgressDialog(this, getString(R.string.empty));
        ArrayList<String> accountList = new ArrayList<>();
        accountList.add(account);
        NIMClient.getService(TeamService.class).removeManagers(teamId, accountList).setCallback(
                new RequestCallback<List<TeamMember>>() {

                    @Override
                    public void onSuccess(List<TeamMember> members) {
                        DialogMaker.dismissProgressDialog();
                        identity.setText(R.string.team_member);
                        ToastHelper.showToastLong(AdvancedTeamMemberInfoActivity.this, R.string.update_success);
                        viewMember = members.get(0);
                        updateMemberInfo();
                    }

                    @Override
                    public void onFailed(int code) {
                        DialogMaker.dismissProgressDialog();
                        ToastHelper.showToastLong(AdvancedTeamMemberInfoActivity.this,
                                String.format(getString(R.string.update_failed), code));
                    }

                    @Override
                    public void onException(Throwable exception) {
                        DialogMaker.dismissProgressDialog();
                    }
                });
    }

    /**
     * 移除群成员确认
     */
    private void showConfirmButton() {
        EasyAlertDialogHelper.OnDialogActionListener listener = new EasyAlertDialogHelper.OnDialogActionListener() {

            @Override
            public void doCancelAction() {
            }

            @Override
            public void doOkAction() {
                removeMember();
            }
        };
        final EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(this, null, getString(
                        R.string.team_member_remove_confirm), getString(R.string.remove), getString(R.string.cancel), true,
                listener);
        dialog.show();
    }

    /**
     * 移除群成员
     */
    private void removeMember() {
        DialogMaker.showProgressDialog(this, getString(R.string.empty));
        NIMClient.getService(TeamService.class).removeMember(teamId, account).setCallback(new RequestCallback<Void>() {

            @Override
            public void onSuccess(Void param) {
                DialogMaker.dismissProgressDialog();
                makeIntent(account, isSetAdmin, true);
                finish();
                ToastHelper.showToastLong(AdvancedTeamMemberInfoActivity.this, R.string.update_success);
            }

            @Override
            public void onFailed(int code) {
                DialogMaker.dismissProgressDialog();
                ToastHelper.showToastLong(AdvancedTeamMemberInfoActivity.this,
                        String.format(getString(R.string.update_failed), code));
            }

            @Override
            public void onException(Throwable exception) {
                DialogMaker.dismissProgressDialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        makeIntent(account, isSetAdmin, false);
        super.onBackPressed();
    }

    /**
     * 设置返回的Intent
     *
     * @param account    帐号
     * @param isSetAdmin 是否设置为管理员
     * @param value      是否移除群成员
     */
    private void makeIntent(String account, boolean isSetAdmin, boolean value) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID, account);
        intent.putExtra(EXTRA_ISADMIN, isSetAdmin);
        intent.putExtra(EXTRA_ISREMOVE, value);
        setResult(RESULT_OK, intent);
    }

    private boolean isSelf(String account) {
        return NimUIKit.getAccount().equals(account);
    }
}
