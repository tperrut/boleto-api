package com.boleto.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.boleto.api.model.Boleto;


@Repository
public interface BoletoRepository extends JpaRepository<Boleto,String>{
}
