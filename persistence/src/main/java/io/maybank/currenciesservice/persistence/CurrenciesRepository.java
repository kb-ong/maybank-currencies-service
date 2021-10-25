package io.maybank.currenciesservice.persistence;

import io.maybank.currenciesservice.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrenciesRepository extends JpaRepository<Currency, Long> {

    Currency findByCode(String currencyCode);
    @Query(value = "select * from currency order by id limit :pagesize offset :offset", nativeQuery = true)
    List<Currency> getCurrenciesByPagination(@Param("pagesize") int pagesize,@Param("offset") int offset);

}
