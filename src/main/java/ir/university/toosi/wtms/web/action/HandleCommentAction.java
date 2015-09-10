package ir.university.toosi.wtms.web.action;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.service.CommentServiceImpl;
import ir.university.toosi.wtms.web.action.monitoring.HandleMonitoringAction;
import ir.university.toosi.tms.model.entity.Comment;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;

import javax.ejb.EJB;
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

    @EJB
    private CommentServiceImpl commentService;
    private int page = 1;
    private String message = "";
    private List<Comment> comments = null;
    private Comment currentComment = null;

    public void beginAuthorize() {
        me.setActiveMenu(MenuType.SENTRY);

        refreshAuthorize();
    }

    public void begin() {
        me.setActiveMenu(MenuType.SENTRY);
        refresh();
    }

    public void submit() {
        Comment comment
                = new Comment();
        comment.setTrafficLog(handleMonitoringAction.getCurrentTrafficLog());
        comment.setMessage(message);
        comment.setAuthorize(false);
        comment.setEffectorUser(me.getUsername());
        commentService.createComment(comment);

    }

    private void refreshAuthorize() {
        comments = commentService.getAllAuthorizeComment();
        page = 1;

    }

    private void refresh() {
        if ("admin".equalsIgnoreCase(me.getUsername())) {
            comments = commentService.getAllComment();
        } else {
            comments = commentService.findByEffectorUser(me.getUsername());
        }
        page = 1;

    }

    public void accept() {
        currentComment.setAuthorize(true);
        commentService.editComment(currentComment);
        me.addInfoMessage("operation.occurred");

        refreshAuthorize();

    }

    public void refuse() {
        commentService.rejectComment(currentComment);
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

    public Comment getCurrentComment() {
        return currentComment;
    }

    public void setCurrentComment(Comment currentComment) {
        this.currentComment = currentComment;
    }
}