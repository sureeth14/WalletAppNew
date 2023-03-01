package com.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService{

    @Autowired
    private WalletJpaRepository walletRepository;


    @Override
    public WalletDto registerWallet(WalletDto wallet) throws WalletException {
        return this.walletRepository.save(wallet);
    }

    @Override
    public WalletDto getWalletById(Integer walletId) throws WalletException {
        Optional<WalletDto> employeeOptional = this.walletRepository.findById(walletId);
        if(employeeOptional.isEmpty())
            throw new WalletException("Employee could not be found id:"+walletId);

        return employeeOptional.get();
    }

    @Override
    public WalletDto updateWallet(WalletDto wallet) throws WalletException {
        Optional<WalletDto> opt=this.walletRepository.findById(wallet.getId());
        if(opt.isEmpty())
            throw new WalletException("No Such Wallet account found with Id:"+wallet.getId());
        return this.walletRepository.save(wallet);

    }

    @Override
    public WalletDto deleteWalletById(Integer walletId) throws WalletException {
        Optional<WalletDto> opt=this.walletRepository.findById(walletId);
        if(opt.isEmpty())
            throw new WalletException("No Such Wallet account found with Id:"+walletId);
        WalletDto wallet=opt.get();
        this.walletRepository.delete(wallet);
    return wallet;
    }


    @Override
    public Double addFundsToWalletById(Integer walletId, Double amount) throws WalletException {

        Optional<WalletDto> optionalWallet = walletRepository.findById(walletId);
        if(optionalWallet.isEmpty())
            throw new WalletException("Wallet does not exists to add funds, id:"+walletId);

        WalletDto wallet = optionalWallet.get();
        Double currentBalance = wallet.getBalance();
        Double newBalance = currentBalance + amount;
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
        return newBalance;
    }

    @Override
    public Double withdrawFundsFromWalletById(Integer walletById, Double amount) throws WalletException {
        Optional<WalletDto> optionalWallet = walletRepository.findById(walletById);
        if(optionalWallet.isEmpty())
            throw new WalletException("Wallet does not exists to withdraw funds , id:" + walletById);

        WalletDto wallet = optionalWallet.get();
        Double currentBalance = wallet.getBalance();
        if(currentBalance < amount)
            throw new WalletException("Insufficient balance, current balance:" + currentBalance);
        currentBalance -= amount;
        wallet.setBalance(currentBalance);
        this.walletRepository.save(wallet);
       return null;
    }

    @Override
    public Boolean fundTransfer(Integer fromWalletId, Integer toWalletId, Double amount) throws WalletException {

        Optional<WalletDto> fromWallet = this.walletRepository.findById(fromWalletId);
        if(fromWallet.isEmpty())
            throw new WalletException("From wallet does not exists, id:"+fromWalletId);
        Optional<WalletDto> toWallet = this.walletRepository.findById(toWalletId);
        if(toWallet.isEmpty())
            throw new WalletException("To wallet does not exists, id:"+toWalletId);

        WalletDto wallet = fromWallet.get();
        Double fromBalance = wallet.getBalance();
        if(fromBalance < amount)
            throw new WalletException("Insufficient balance, current balance:" + fromBalance);

        wallet.setBalance(fromBalance - amount);

        WalletDto wallet1 = toWallet.get();
        Double toBalance = wallet1.getBalance();
        wallet1.setBalance(toBalance+amount);

        this.walletRepository.save(wallet);
        this.walletRepository.save(wallet1);
        return true;
    }

    @Override
    public Collection<WalletDto> getAllWallets() {
        return this.walletRepository.findAll();
    }
}
