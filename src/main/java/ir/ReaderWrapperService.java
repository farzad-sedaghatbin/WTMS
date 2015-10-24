package ir;

import ir.university.toosi.tms.model.service.EJB3RemoteInterface;
import ir.university.toosi.tms.readerwrapper.AccessEventData;
import ir.university.toosi.tms.readerwrapper.GetAccessEventDataDelegate;
import ir.university.toosi.tms.readerwrapper.Person;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

/**
 * Created by farzad on 10/21/2015.
 */
@Stateless
@Remote(IReaderWrapperService.class)
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class ReaderWrapperService implements IReaderWrapperService {


    @Override
    public void forceOpenDoor(int terminalId) {

    }

    @Override
    public void lockDoor(int terminalId) {

    }

    @Override
    public void unLockDoor(int terminalId) {

    }

    @Override
    public void lockTerminal(int terminalId) {

    }

    @Override
    public void unLockTerminal(int terminalId) {

    }

    @Override
    public void getUserList(int terminalId) {

    }

    @Override
    public void setUserList(int terminalId,List<Person> personList,boolean isSucceed,String failedMessage) {
        System.out.println(personList.size());

    }

    @Override
    public void addOnGetAccessEventData(int terminalId, AccessEventData value) {
        System.out.println(value.getUserID());

    }
}
