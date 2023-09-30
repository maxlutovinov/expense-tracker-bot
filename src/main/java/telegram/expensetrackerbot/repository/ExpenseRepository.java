package telegram.expensetrackerbot.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import telegram.expensetrackerbot.model.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query(value = "from Expense e where e.expenseDate between ?1 and ?2 "
            + "order by e.userAccount, e.expenseDate")
    List<Expense> findAllByExpenseDateBetween(LocalDate from, LocalDate to);
}
