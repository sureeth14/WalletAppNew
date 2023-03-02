package com.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface WalletJpaRepository extends JpaRepository<WalletDto,Integer> {

    //creation of custom queries by method name using keyWords

    List<WalletDto> findByName(String name);
    List<WalletDto> findByNameContaining(String name);


    //by writing JPQL queries

    @Query("SELECT wallet FROM WalletDto wallet")
    List<WalletDto> getAllWallets();

    @Query("SELECT wallet FROM WalletDto wallet WHERE wallet.name LIKE : name ")
    List<WalletDto> getAllWalletsHavingNameLike(String name);

}
