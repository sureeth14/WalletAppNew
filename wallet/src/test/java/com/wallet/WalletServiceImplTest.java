package com.wallet;


import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.BDDMockito.given;


@SpringBootTest
public class WalletServiceImplTest {

    @Autowired
    private WalletService walletService;

    @Mock
    private WalletJpaRepository walletJpaRepository;


    @Test
    public void testServiceWithoutActualRepository() throws WalletException {
        given(this.walletJpaRepository.findById(1))
                .willReturn(Optional.of(new WalletDto(1,"sureeth",3500.0)));
        assertEquals("sureeth",walletService.getWalletById(1).getName());
    }



    @Test
    public void testGetWalletThrowsExceptionTest() throws WalletException {
        given(this.walletJpaRepository.findById(1))
                .willReturn(null);
        assertThrows(WalletException.class,()->walletService.getWalletById(1));
    }

    @Test
    public void addFundsToWalletTest() throws WalletException{

        given(this.walletJpaRepository.findById(200))
                .willReturn(Optional.of(new WalletDto(200, "Ford 1", 2000.0)));
        given(this.walletJpaRepository.findById(400))
                .willReturn(Optional.of(new WalletDto(400, "Ford 2", 4000.0)));

        Double newBalance=this.walletService.addFundsToWalletById(200,250.0);
        assertEquals(Optional.of(2250.0),newBalance);
    }

    @Test
    public void withdrawFundsFromWalletTest() throws WalletException{

        given(this.walletJpaRepository.findById(200))
                .willReturn(Optional.of(new WalletDto(200, "Ford 1", 2000.0)));
        given(this.walletJpaRepository.findById(400))
                .willReturn(Optional.of(new WalletDto(400, "Ford 2", 4000.0)));

        Double newBalance=this.walletService.withdrawFundsFromWalletById(200,250.0);
        assertEquals(Optional.of(1750.0),newBalance);
    }
    @Test
    public void withdrawFundsFromWalletInsufficientFundExceptionTest() throws WalletException{

        given(this.walletJpaRepository.findById(200))
                .willReturn(Optional.of(new WalletDto(200, "Ford 1", 2000.0)));
        given(this.walletJpaRepository.findById(400))
                .willReturn(Optional.of(new WalletDto(400, "Ford 2", 4000.0)));


        assertThrows(WalletException.class,()->this.walletService.withdrawFundsFromWalletById(200,2500.0));
    }
    @Test
    public void withdrawFundsFromWalletInsufficientFundExceptionMessageTest() throws WalletException{

        given(this.walletJpaRepository.findById(200))
                .willReturn(Optional.of(new WalletDto(200, "Ford 1", 2020.0)));
        given(this.walletJpaRepository.findById(400))
                .willReturn(Optional.of(new WalletDto(400, "Ford 2", 4000.0)));
        String eMessage="";
        try{
            this.walletService.withdrawFundsFromWalletById(200,2500.0);
        }
        catch (WalletException e){
            eMessage=e.getMessage();
        }
        assertEquals("Insufficient balance, current balance:2020.0",eMessage);
    }
}
