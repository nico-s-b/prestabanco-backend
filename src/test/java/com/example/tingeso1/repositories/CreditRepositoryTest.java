package com.example.tingeso1.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.example.tingeso1.entities.Credit;

@DataJpaTest
@ActiveProfiles("test")
public class CreditRepositoryTest {

    @Autowired
    TestEntityManager entityManagerntityManager;

    @Autowired
    private CreditRepository creditRepository;
/*
    @Test
    public void whenFindById_thenReturnCredit() {
        //given
        Credit credit = new Credit(
            id = null;
            
            );
        entityManager.persistAndFlush(credit);

        //when
        Credit found = creditRepository.findById(credit.getId());

        //then
        assertThat(found.getId()).isEqualTo(credit.getId());
    }
*/
}
