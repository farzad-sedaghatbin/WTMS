package ir;


import ir.university.toosi.tms.readerwrapper.AccessEventData;
import ir.university.toosi.tms.readerwrapper.GetAccessEventDataDelegate;
import ir.university.toosi.tms.readerwrapper.Person;
import ir.university.toosi.tms.readerwrapper.PersonHolder;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

/**
 * Created by Rahman on 10/21/15.
 */
@WebService
public interface IReaderWrapperService {


    @WebMethod
    public  void forceOpenDoor(int terminalId);
    @WebMethod
    public  void lockDoor(int terminalId);

    @WebMethod
    public  void unLockDoor(int terminalId);

    @WebMethod
    public  void lockTerminal(int terminalId);

    @WebMethod
    public  void unLockTerminal(int terminalId);

    @WebMethod
    public void getUserList(int terminalId);

    @WebMethod
    public void setUserList(int terminalId, PersonHolder personHolder);

    @WebMethod
    public void addOnGetAccessEventData(int terminalId, ir.university.toosi.tms.readerwrapper.AccessEventData accessEventData);

    @WebMethod
    public boolean addOnGetOnlineVerifyAccessControl(int terminalId, ir.university.toosi.tms.readerwrapper.AccessEventData accessEventData) ;

    @WebMethod
    public void addUserInfo(int terminal, PersonHolder personHolder);
}
