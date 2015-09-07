package ir.university.toosi.wtms.web.action.language;

import org.richfaces.event.FileUploadEvent;

import javax.inject.Named;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 8/19/13
 * Time: 3:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Named(value = "fileUpload")
public class FileUpload implements Serializable {

    public String begin(){
        return "addLanguage";
    }

    public void listener(FileUploadEvent event) throws Exception {
//        UploadedFile item = event.getUploadedFile();
//        Language file = new Language();
//        file.setName(item.getName());
//        file.setContent(item.getData());
//        file.setRtl(true);
    }


}
