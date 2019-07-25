package com.rmith.service;

//<editor-fold defaultstate="collapsed" desc="IMPORT">
import com.rmith.dto.AccountDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
//</editor-fold>

/**
 *
 * @author Teo-Em
 */
@Service
@Lazy
public class AccountServiceImpl implements AccountService{

    //<editor-fold defaultstate="collapsed" desc="GET LIST ACCOUNT">
    @Override
    public List<AccountDTO> getListAccount() {
        List<AccountDTO> list = new ArrayList<>();
        AccountDTO dto1 = new AccountDTO();
        dto1.setAccountId(1);
        dto1.setFirstName("Tony");
        dto1.setLastName("nguyen");
        dto1.setIsAdmin(1);
        dto1.setEmail("tonynguyen@gmail.com");
        dto1.setMemo("Hello kitty!");
        
        AccountDTO dto2 = new AccountDTO();
        dto2.setAccountId(2);
        dto2.setFirstName("Tony");
        dto2.setLastName("Teo");
        dto2.setIsAdmin(0);
        dto2.setEmail("tonyteo@gmail.com");
        dto2.setMemo("Hello world!");
        list.add(dto1);
        list.add(dto2);
        return list;
    }
    //</editor-fold>
 
}
