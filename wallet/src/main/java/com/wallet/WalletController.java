package com.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    @GetMapping("/")
    public String greet(){
        return "Hello welcome to wallet app.";
    }

    @PostMapping("wallet")
    public WalletDto registerWallet(@RequestBody WalletDto wallet) throws WalletException {
        return this.walletService.registerWallet(wallet);
    }

    @GetMapping("wallet/{id}")
    public WalletDto getWalletById(@PathVariable("id") Integer walletId) throws WalletException {
        return this.walletService.getWalletById(walletId);
    }
    @PutMapping("wallet")
    public WalletDto updateWallet(@RequestBody WalletDto wallet) throws WalletException {
        return this.walletService.updateWallet(wallet);
    }
    @DeleteMapping("wallet/{id}")
    public WalletDto deleteWallet(@PathVariable("id") Integer walletId) throws WalletException {
        return this.walletService.deleteWalletById(walletId);
    }

    @PatchMapping("wallet/{id}/{amount}")
    public Double addFundsToWalletById(@PathVariable("id")Integer walletId,@PathVariable("amount") Double amount) throws WalletException {
        return this.walletService.addFundsToWalletById(walletId,amount);
    }
    @PatchMapping("wallet/{id}/fund/{amount}")
    public Double withdrawFundsfromWalletById(@PathVariable("id")Integer walletId,@PathVariable("amount") Double amount) throws WalletException {
        return this.walletService.withdrawFundsFromWalletById(walletId,amount);
    }

    @PostMapping("wallet/fund/{fromId}/{toId}/{amount}")
    public Boolean fundTransfer(@PathVariable("fromId")Integer fromWalletId,@PathVariable("toId")Integer toWalletId , @PathVariable("amount") Double amount) throws WalletException {
        return this.walletService.fundTransfer(fromWalletId,toWalletId,amount);
    }


    @GetMapping("wallets")
    public Collection<WalletDto> getAllWallets(){
        return this.walletService.getAllWallets();
    }



    @Autowired
    private WalletJpaRepository walletJpaRepository;


    @GetMapping("custom/wallets")
    public List<WalletDto> findAllWallets(){
        return this.walletJpaRepository.getAllWallets();
    }


    @GetMapping("custom/wallet/{name}")
    public List<WalletDto> findAllWalletsHavingName(@PathVariable("name") String name) {
        return this.walletJpaRepository.getAllWalletsHavingNameLike("%" + name + "%");
    }
}
