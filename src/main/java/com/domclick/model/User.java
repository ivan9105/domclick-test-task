package com.domclick.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "BANK_USER")
public class User extends BaseEntity {
    @NotNull(message = "Имя не может быть пустым и превышать 255 символов")
    @Size(min = 1, max = 255, message = "Имя не может быть пустым и превышать 255 символов")
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;

    @NotNull(message = "Фамилия не может быть пустой и превышать 255 символов")
    @Size(min = 1, max = 255, message = "Фамилия не может быть пустой и превышать 255 символов")
    @Column(name = "LAST_NAME", nullable = false)
    private String lastName;

    @NotNull(message = "Отчество не может быть пустым и превышать 255 символов")
    @Size(min = 1, max = 255, message = "Отчество не может быть пустым и превышать 255 символов")
    @Column(name = "MIDDLE_NAME", nullable = false)
    private String middleName;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Account> accounts = new HashSet<>();

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return String.format("%s %s %s", lastName, firstName, middleName);
    }
}
