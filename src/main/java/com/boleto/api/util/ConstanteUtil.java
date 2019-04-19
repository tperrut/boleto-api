package com.boleto.api.util;

public class ConstanteUtil {
	
	//Home
	public static final String WELCOME = "Welcome to Boleto-API";
	public static final String SWAGGER = "swagger-ui.html";
	
	//Logger
	public static final String CANCELAR_BOLETO = " | - CANCELAR_BOLETO - | para o id: ";
	public static final String DETALHAR_BOLETO = " | - DETALHAR_BOLETO - | para o id: ";
	public static final String PAGAR_BOLETO = " | - PAGAR_BOLETO - | para o id: ";
	public static final String CRIAR_BOLETO = " | - CRIAR_BOLETO - | para o Cliente: " ;
	public static final String FIND_BY_NAME = " | - FIND_BY_NAME - | para o cliente: ";
	public static final String LISTAR_BOLETOS = " | - LISTAR_BOLETOS - |";
	public static final String LISTAR_BOLETOS_PAGED =  " | - LISTAR_BOLETOS_PAGED - |"; 
	
	//exception
	public static final String BOLETO_NOT_FOUND = "Boleto não encontrado para o ID: ";
	public static final String BOLETO_NOT_FOUND_BY_CLIENTE ="Boleto não encontrado para o CLIENTE ";

	//Erro 
	public static final String ERRO ="Erro: ";
	public static final String INTERNAL_SERVER_ERROR= "INTERNAL_SERVER_ERROR : ";
	public static final String ERRO_BOLETO_POR_NOME= "Erro ao consultar boleto por nome. Contate o admin!";
	public static final String ERRO_LISTAR_BOLETO= " ao listar boletos. Contate o admin!";
	public static final String ERRO_PAGAR = "Erro ao Pagar boleto. Contate o admin!";
	public static final String ERRO_CRIAR_BOLETO="Erro ao criar boleto. Contate o admin!";
	public static final String ERRO_CANCELAR_BOLETO="Erro ao Cancelar boleto. Contate o admin!";
	public static final String ERRO_NOT_PENDING = "Boleto deve estar com o status PENDING";
	

	public static final String USER_DETAIL_NOME = "USER_DETAIL:  Nome: ";
	public static final String BOLETO_TESTE = "boleto_teste";
	
}	