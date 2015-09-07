package ir.university.toosi.wtms.web.action;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.monitoring.HandleMonitoringAction;
import ir.university.toosi.wtms.web.model.entity.Comment;
import ir.university.toosi.wtms.web.model.entity.MenuType;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleCommentAction")
@SessionScoped
public class HandleCommentAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleTrafficAction handleTrafficAction;
    @Inject
    private HandleMonitoringAction handleMonitoringAction;
    private int page = 1;
    private String message = "";
    private DataModel<Comment> commentDataModel = null;
    private List<Comment> comments = null;

    public String beginAuthorize() {
        me.setActiveMenu(MenuType.SENTRY);

        refreshAuthorize();
        return "authorize";
    }

    public String begin() {
        me.setActiveMenu(MenuType.SENTRY);

        refresh();
        return "comments";
    }

    public void submit() {
        Comment comment
                = new Comment();
        comment.setTrafficLog(handleMonitoringAction.getCurrentTrafficLog());
        comment.setMessage(message);
        comment.setAuthorize(false);
        comment.setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createComment");
        try {
            comment = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(comment)), Comment.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void refreshAuthorize() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllAuthorizeComment");
        try {
            comments = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Comment>>() {
            });
            commentDataModel = new ListDataModel<>(comments);
        } catch (IOException e) {
            e.printStackTrace();
        }
        page = 1;
        comments = new ArrayList<>();

    }

    private void refresh() {
        if ("admin".equalsIgnoreCase(me.getUsername())) {
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllComment");
            try {
                comments = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Comment>>() {
                });
                commentDataModel = new ListDataModel<>(comments);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/findByEffectorUser");
            try {
                comments = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), me.getUsername()), new TypeReference<List<Comment>>() {
                });
                commentDataModel = new ListDataModel<>(comments);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        page = 1;
        comments = new ArrayList<>();

    }

    public void accept() {
        Comment comment = commentDataModel.getRowData();
        comment.setAuthorize(true);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editComment");
        try {
            String c = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(comment)), String.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        me.addInfoMessage("operation.occurred");

        refreshAuthorize();

    }

    public void refuse() {
        Comment comment = commentDataModel.getRowData();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/rejectComment");
        try {
            String c = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(comment)), String.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        me.addInfoMessage("operation.occurred");
        refreshAuthorize();

    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public DataModel<Comment> getCommentDataModel() {
        return commentDataModel;
    }

    public void setCommentDataModel(DataModel<Comment> commentDataModel) {
        this.commentDataModel = commentDataModel;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}