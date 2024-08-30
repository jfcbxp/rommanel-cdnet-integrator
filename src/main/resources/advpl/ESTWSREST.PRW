#Include 'protheus.ch'
#Include 'FWMVCDEF.ch'
#Include "RestFul.CH"
#include "tbiconn.ch"

User Function ESTWSREST()	
Return

WSRESTFUL ESTCDNET DESCRIPTION "ServiÃ§o REST para estoque"
WSDATA METODO As String //String que vamos receber via URL
 
WSMETHOD POST DESCRIPTION "Em caso de sucesso, retorna o mesmo objeto enviado" WSSYNTAX "/ESTCDNET || /ESTCDNET/{METODO}" //Disponibilizamos um mÃ©todo do tipo GET
 
END WSRESTFUL

WSMETHOD POST WSRECEIVE METODO WSSERVICE ESTCDNET
	//--> Recuperamos o produto informado via URL
	//--> Podemos fazer dessa forma ou utilizando o atributo ::aUrlParms, que Ã© um array com os parÃ¢metros recebidos via URL (QueryString)
	Local cBody := ::GetContent()
	Local aArea		:= GetArea()
	Local aItens
	Local nIt,nPr

	oJson := JsonObject():New()
	ret := oJson:FromJson(cBody)

/*
 GRUPO SBM
AY0 1 FAMILIA
AY0 2 COR
AY0 3 TEMA
AY0 4 ESTILO
AY0 6 ACESSORIO
AY0 5 FORMATO
 CAMPANHA AY2
*/

	// define o tipo de retorno do mÃ©todo
	::SetContentType("application/json")
	IF oJson:GetJSonObject('metodo') == "/incluirFamilia"
		IF !EMPTY(oJson['data']['codigoFamilia'])
			cadFamilia(oJson['data']['codigoFamilia'],oJson['data']['descricao'])
			::SetResponse(oJson:toJSON())

		ENDIF
	ELSEIF oJson:GetJSonObject('metodo') == "/incluirFormato"
		IF !EMPTY(oJson['data']['codigoFormato'])
			cadFormato(oJson['data']['codigoFormato'],oJson['data']['descricao'])
			::SetResponse(oJson:toJSON())

		ENDIF

	ELSEIF oJson:GetJSonObject('metodo') == "/incluirAcessorio"
		IF !EMPTY(oJson['data']['codigoAcessorio'])
			cadAcessorio(oJson['data']['codigoAcessorio'],oJson['data']['descricao'])
			::SetResponse(oJson:toJSON())

		ENDIF

	ELSEIF oJson:GetJSonObject('metodo') == "/incluirEstilo"
		IF !EMPTY(oJson['data']['codigoEstilo'])
			cadEstilo(oJson['data']['codigoEstilo'],oJson['data']['descricao'])
			::SetResponse(oJson:toJSON())

		ENDIF

	ELSEIF oJson:GetJSonObject('metodo') == "/incluirTema"
		IF !EMPTY(oJson['data']['codigoTema'])
			cadTema(oJson['data']['codigoTema'],oJson['data']['descricao'])
			::SetResponse(oJson:toJSON())

		ENDIF

	ELSEIF oJson:GetJSonObject('metodo') == "/incluirCor"
		IF !EMPTY(oJson['data']['codigoCor'])
			cadCor(oJson['data']['codigoCor'],oJson['data']['descricao'])
			::SetResponse(oJson:toJSON())

		ENDIF


	ELSEIF oJson:GetJSonObject('metodo') == "/incluirGrupo"
		IF !EMPTY(oJson['data']['codigoGrupo'])
			cadGrupo(oJson['data']['codigoGrupo'],oJson['data']['descricao'])
			::SetResponse(oJson:toJSON())

		ENDIF

	ELSEIF oJson:GetJSonObject('metodo') == "/incluirCampanha"
		IF !EMPTY(oJson['data']['codigoCampanha'])
			cadCamp(oJson['data']['codigoCampanha'],oJson['data']['descricao'])
			::SetResponse(oJson:toJSON())

		ENDIF
	ELSEIF oJson:GetJSonObject('metodo') == "/incluirPedidoDistribuidor"
		IF !EMPTY(oJson['pedidoDistribuidorCDNet']['codigoPedidoWeb'])
			aItens := oJson['pedidoDistribuidorCDNet']['itens']
			cCnpj := Alltrim(oJson['pedidoDistribuidorCDNet']['cnpjLoja'])
			cCnpj := StrTran(cCnpj,'.','')
			cCnpj := StrTran(cCnpj,'-','')
			cCnpj := StrTran(cCnpj,'/','')
			_cFilDes := "0107"

			//DETERMINA FILIAL DO PEDIDO PELO CNPJ
			SM0->(DbGoTop())
			DO WHILE !SM0->(Eof())
				if ALLTRIM(SM0->M0_CGC) = cCnpj
					_cFilDes :=  PadR(SM0->M0_CODFIL,4)
					exit
				endif
				SM0->(DbSkip())
			ENDDO
			DbSelectArea("ZAT")
			ZAT->(DbSetOrder(1))
			IF !ZAT->(DbSeek(_cFilDes+PadR(oJson['pedidoDistribuidorCDNet']['codigoPedidoWeb'],TamSX3("ZAT_CODIGO")[1])))
				RecLock("ZAT",.T.)
				ZAT->ZAT_FILIAL := _cFilDes
				ZAT->ZAT_CODIGO := PadR(oJson['pedidoDistribuidorCDNet']['codigoPedidoWeb'],TamSX3("ZAT_CODIGO")[1])
				ZAT->ZAT_USUCDN := PadR(oJson['pedidoDistribuidorCDNet']['loginUsuario'],TamSX3("ZAT_USUCDN")[1])
				ZAT->ZAT_COMENT := NoAcento(PadR(oJson['pedidoDistribuidorCDNet']['comentarios'],TamSX3("ZAT_COMENT")[1]))
				ZAT->ZAT_FORMPG := NoAcento(PadR(oJson['pedidoDistribuidorCDNet']['formaPagamento'],TamSX3("ZAT_FORMPG")[1]))
				ZAT->ZAT_DESCRI := NoAcento(PadR(oJson['pedidoDistribuidorCDNet']['descricaoPedido'],TamSX3("ZAT_DESCRI")[1]))
				ZAT->ZAT_CNPJ 	:= NoAcento(PadR(oJson['pedidoDistribuidorCDNet']['cnpjLoja'],TamSX3("ZAT_CNPJ")[1]))
				ZAT->ZAT_TOTAL 	:= oJson['pedidoDistribuidorCDNet']['total']
				ZAT->ZAT_TOTCDE := oJson['pedidoDistribuidorCDNet']['totalComDesconto']
				ZAT->ZAT_TOTCDE := oJson['pedidoDistribuidorCDNet']['totalComDesconto']
				ZAT->ZAT_DTCAD  := STOD(oJson['pedidoDistribuidorCDNet']['dataCriacao'])
				ZAT->ZAT_DTATUA := STOD(oJson['pedidoDistribuidorCDNet']['dataAtualizacao'])
				ZAT->ZAT_DTAGEN := STOD(oJson['pedidoDistribuidorCDNet']['dataAgendamento'])
				ZAT->(MsUnLock())

				CONOUT("CADASTRANDO PEDIDO "+oJson['pedidoDistribuidorCDNet']['codigoPedidoWeb'])

				For nIt := 1 To Len(aItens)
					DbSelectArea("ZAU")
					ZAU->(DbSetOrder(1))
					IF !ZAU->(DbSeek(_cFilDes+PadR(oJson['pedidoDistribuidorCDNet']['codigoPedidoWeb'],TamSX3("ZAU_CODIGO")[1])+PadR(aItens[nIt]['codigoProdutoCompleto'],TamSX3("ZAU_PRODUT")[1])))
						RecLock("ZAU",.T.)
						ZAU->ZAU_FILIAL := _cFilDes
						ZAU->ZAU_CODIGO := PadR(oJson['pedidoDistribuidorCDNet']['codigoPedidoWeb'],TamSX3("ZAU_CODIGO")[1])
						ZAU->ZAU_SUBPRO := PadR(aItens[nIt]['codigoSubProduto'],TamSX3("ZAU_SUBPRO")[1])
						ZAU->ZAU_PRODUT := PadR(aItens[nIt]['codigoProdutoCompleto'],TamSX3("ZAU_PRODUT")[1])
						ZAU->ZAU_QTDPED := aItens[nIt]['quantidadePedida']
						ZAU->ZAU_VLRTDE := aItens[nIt]['valorTotalDesconto']
						ZAU->ZAU_VLRTOT := aItens[nIt]['valorTotal']
						ZAU->ZAU_DELFAB := IIF(aItens[nIt]['removidoPedidoFabrica'],"S","N")
						ZAU->(MsUnLock())
					ENDIF
				Next
			ENDIF


			::SetResponse(oJson:toJSON())

		ENDIF
	ELSEIF oJson:GetJSonObject('metodo') == "/incluirNotaFiscalDistribuidor"
		IF !EMPTY(oJson['notaFiscalCDNet']['chave'])
			aItens := oJson['notaFiscalCDNet']['itens']
			cCnpj := Alltrim(oJson['notaFiscalCDNet']['cnpj'])
			cCnpj := StrTran(cCnpj,'.','')
			cCnpj := StrTran(cCnpj,'-','')
			cCnpj := StrTran(cCnpj,'/','')
			_cFilDes := "0107"

			//DETERMINA FILIAL DO PEDIDO PELO CNPJ
			SM0->(DbGoTop())
			DO WHILE !SM0->(Eof())
				if ALLTRIM(SM0->M0_CGC) = cCnpj
					_cFilDes :=  PadR(SM0->M0_CODFIL,4)
					exit
				endif
				SM0->(DbSkip())
			ENDDO
			DbSelectArea("ZAV")
			ZAV->(DbSetOrder(1))
			IF !ZAV->(DbSeek(_cFilDes+PadR(oJson['notaFiscalCDNet']['chave'],TamSX3("ZAV_CHAVE")[1])))
				RecLock("ZAV",.T.)
				ZAV->ZAV_FILIAL := _cFilDes
				ZAV->ZAV_DOC    := PadR(oJson['notaFiscalCDNet']['numero'],TamSX3("ZAV_DOC")[1])
				ZAV->ZAV_CHAVE  := PadR(oJson['notaFiscalCDNet']['chave'],TamSX3("ZAV_CHAVE")[1])
				ZAV->ZAV_NATOPE := NoAcento(PadR(oJson['notaFiscalCDNet']['naturezaOperacao'],TamSX3("ZAV_NATOPE")[1]))
				ZAV->ZAV_CNPJ   := NoAcento(PadR(oJson['notaFiscalCDNet']['cnpj'],TamSX3("ZAV_CNPJ")[1]))
				ZAV->ZAV_TOTAL 	:= oJson['notaFiscalCDNet']['valorNota']
				ZAV->ZAV_EMISSA := STOD(oJson['notaFiscalCDNet']['dataEmissao'])
				IF !EMPTY(oJson['notaFiscalCDNet']['dataCancelada'])
					ZAV->ZAV_DTCANC := STOD(oJson['notaFiscalCDNet']['dataCancelada'])
				ENDIF
				ZAV->ZAV_CANC   := IIF(oJson['notaFiscalCDNet']['cancelada'],"S","N")
				ZAV->(MsUnLock())
				CONOUT("CADASTRANDO NOTA FISCAL "+oJson['notaFiscalCDNet']['chave'])

				For nIt := 1 To Len(aItens)
					aProds := aItens[nIt]['produtos']
					For nPr := 1 To Len(aProds)
						DbSelectArea("ZAX")
						ZAX->(DbSetOrder(1))
						IF !ZAX->(DbSeek(_cFilDes+PadR(oJson['notaFiscalCDNet']['chave'],TamSX3("ZAX_CHAVE")[1])+PadR(aProds[nPr]['codigo'],TamSX3("ZAX_PRODUT")[1])))
							RecLock("ZAX",.T.)
							ZAX->ZAX_FILIAL := _cFilDes
							ZAX->ZAX_DOC    := PadR(oJson['notaFiscalCDNet']['numero'],TamSX3("ZAX_DOC")[1])
							ZAX->ZAX_CHAVE  := PadR(oJson['notaFiscalCDNet']['chave'],TamSX3("ZAX_CHAVE")[1])
							ZAX->ZAX_PEDSIM := PadR(aItens[nIt]['pedidoSIMMA'],TamSX3("ZAX_PEDSIM")[1])
							ZAX->ZAX_PEDCDN := PadR(aItens[nIt]['pedidoCDNET'],TamSX3("ZAX_PEDCDN")[1])
							ZAX->ZAX_PRODUT := PadR(aProds[nPr]['codigo'],TamSX3("ZAX_PRODUT")[1])
							ZAX->ZAX_QUANT  := aProds[nPr]['quantidade']
							ZAX->ZAX_VALOR  := aProds[nPr]['valor']
							ZAX->(MsUnLock())
						ENDIF
					Next

				Next
			ENDIF


			::SetResponse(oJson:toJSON())

		ENDIF
	ELSEIF oJson:GetJSonObject('metodo') == "/incluirProduto"
		IF !EMPTY(oJson['data']['codigoProduto'])
			aItens := oJson['data']['subProdutos']
			For nIt := 1 To Len(aItens)
				_cCodProd := PadR(aItens[nIt]['subProduto'],TamSX3("B1_COD")[1])
				if !EMPTY(_cCodProd)
					// Faço o Decode do BASE64 direto para o arquivo tst_1.jpg (a descompactação já é tratada automaticamente)
					IF !EMPTY(oJson['data']['imagemBase64'])
						cDecode64 := Decode64(oJson['data']['imagemBase64'],"\fotos\"+ALLTRIM(_cCodProd)+".jpg",.F.)
					ENDIF
					SB1->(DbSetOrder(1))
					IF !SB1->(DbSeek(xFilial("SB1")+_cCodProd))
						CONOUT("CADASTRANDO PRODUTO "+_cCodProd+" "+NoAcento(PadR(oJson['data']['descricao'],TamSX3("B1_DESC")[1])))
						RecLock("SB1",.T.)
						SB1->B1_COD    := _cCodProd
						SB1->B1_CODBAR := "SEM GTIN"
						SB1->B1_BITMAP := PadR(oJson['data']['codigoProduto'],TamSX3("B1_BITMAP")[1])
						SB1->B1_DESC   := Upper(NoAcento(PadR(oJson['data']['descricao'],TamSX3("B1_DESC")[1])))
						SB1->B1_UM     := "UN"
						SB1->B1_TIPO   := "ME"
						SB1->B1_GRUPO  := ""
						SB1->B1_POSIPI := PadR(oJson['data']['ncm'],TamSX3("B1_POSIPI")[1])
						SB1->B1_ORIGEM := "0"
						SB1->B1_LOCPAD := "01"
						SB1->B1_PRV1   := oJson['data']['precoEtiqueta']
						SB1->B1_CUSTD  := 0
						SB1->B1_LOCALIZ:= "S"
						SB1->B1_GARANT := "2"
						SB1->B1_CEST   := "2803700"
						SB1->B1_TE	   := "001"
						SB1->B1_TS	   := "501"
						SB1->B1_PROC   := "049428"
						SB1->B1_LOJPROC:= "01"
						SB1->B1_IPI	   := 7.8
						SB1->B1_ALIQISS:= 2
						SB1->B1_01CAT1 := ""
						SB1->B1_01CAT2 := ""
						SB1->B1_01CAT3 := ""
						SB1->B1_01CAT4 := ""
						SB1->B1_01CAT5 := ""
						SB1->B1_TIPCONV:= "M"
						SB1->B1_MCUSTD := "1"
						SB1->B1_TIPODEC:= "N"
						SB1->B1_RASTRO := "N"
						SB1->B1_MRP    := "S"
						SB1->B1_CONTRAT:= "N"
						SB1->B1_IMPORT := "N"
						SB1->B1_ANUENTE:= "2"
						SB1->B1_TIPOCQ := "M"
						SB1->B1_SOLICIT:= "N"
						SB1->B1_AGREGCU:= "2"
						SB1->B1_DESPIMP:= "N"
						SB1->B1_INSS   := "N"
						SB1->B1_FLAGSUG:= "1"
						SB1->B1_CLASSVE:= "1"
						SB1->B1_MIDIA  := "2"
						SB1->B1_ENVOBR := "0"
						SB1->B1_ATIVO  := "S"
						SB1->B1_CPOTENC:= "2"
						SB1->B1_USAFEFO:= "1"
						SB1->B1_CRICMS := "0"
						SB1->B1_ESCRIPI:= "3"
						SB1->B1_PIS    := "2"
						SB1->B1_MSBLQL := "2"
						SB1->B1_PRODSBP:= "P"
						SB1->B1_PRN944I:= "S"
						SB1->B1_01CODMA:= ""
						SB1->B1_ATUAPRC:= "S"
						SB1->(MsUnLock())
					ELSE
						CONOUT("ATUALIZANDO PRODUTO "+_cCodProd)
						RecLock("SB1",.F.)
						_cCod := PadR(oJson['data']['codigoFamilia'],TamSX3("AY0_CDNET")[1])
						AY0->(DbSetOrder(4))
						if AY0->(DbSeek(xFilial("AY0")+'1'+_cCod))
							SB1->B1_01CAT1 := AY0->AY0_CODIGO
						Endif
						_cCod := PadR(oJson['data']['codigoCor'],TamSX3("AY0_CDNET")[1])
						AY0->(DbSetOrder(4))
						if AY0->(DbSeek(xFilial("AY0")+'2'+_cCod))
							SB1->B1_01CAT2 := AY0->AY0_CODIGO
						Endif
						_cCod := PadR(oJson['data']['codigoTema'],TamSX3("AY0_CDNET")[1])
						AY0->(DbSetOrder(4))
						if AY0->(DbSeek(xFilial("AY0")+'3'+_cCod))
							SB1->B1_01CAT3 := AY0->AY0_CODIGO
						Endif
						_cCod := PadR(oJson['data']['codigoEstilo'],TamSX3("AY0_CDNET")[1])
						AY0->(DbSetOrder(4))
						if AY0->(DbSeek(xFilial("AY0")+'4'+_cCod))
							SB1->B1_01CAT4 := AY0->AY0_CODIGO
						Endif
						_cCod := PadR(oJson['data']['codigoFormato'],TamSX3("AY0_CDNET")[1])
						AY0->(DbSetOrder(4))
						if AY0->(DbSeek(xFilial("AY0")+'5'+_cCod))
							SB1->B1_01CAT5 := AY0->AY0_CODIGO
						Endif

						_cCod := PadR(oJson['data']['codigoGrupo'],TamSX3("BM_CDNET")[1])
						dbSelectArea("SBM")
						SBM->(DbSetOrder(3))
						if SBM->(DbSeek(xFilial("SBM")+_cCod))
							SB1->B1_GRUPO  := SBM->BM_GRUPO
						Endif

						_cCod := PadR(oJson['data']['codigoCampanha'],TamSX3("AY2_CDNET")[1])
						//FILIAL+CODIGOCDNET
						AY2->(DbSetOrder(3))
						if AY2->(DbSeek(xFilial("AY2")+_cCod))
							SB1->B1_01CODMA:= AY2->AY2_CODIGO

						endif

						SB1->(MsUnLock())

					ENDIF
/*
TABELA 2 46%
TABELA 3 43%
TABELA 4 41,5%
TABELA 5 40%
TABELA U 39%
TABELA V 38%
TABELA W 37%

*/

					SB0->(DbSetOrder(1))
					_cPrc := oJson['data']['precoEtiqueta']
					IF _cPrc > 0
						if !SB0->(DbSeek(xFilial("SB0")+_cCodProd))
								CONOUT("CADASTRANDO PRECO DO PRODUTO "+_cCodProd+" "+NoAcento(PadR(oJson['data']['descricao'],TamSX3("B1_DESC")[1])))
								RecLock("SB0",.T.)
								SB0->B0_COD    := _cCodProd
								SB0->B0_PRV1   := ROUND((_cPrc),2)
								SB0->B0_PRV2   := ROUND((_cPrc)*0.54,2)
								SB0->B0_PRV3   := ROUND((_cPrc)*0.57,2)
								SB0->B0_PRV4   := ROUND((_cPrc)*0.585,2)
								SB0->B0_PRV5   := ROUND((_cPrc)*0.60,2)
								SB0->B0_PRV6   := ROUND((_cPrc)*0.40,2)
								SB0->B0_PRV7   := ROUND((_cPrc)*0.45,2)
								SB0->B0_PRV8   := ROUND((_cPrc)*0.60,2)
								SB0->B0_PRV9   := ROUND((_cPrc)-((_cPrc)*0.8165),2)
								SB0->B0_PRVA   := ROUND((_cPrc)/5.16,2)
								SB0->B0_PRVE   := ROUND((_cPrc)*1.11,2)
								SB0->B0_PRVU   := ROUND((_cPrc)*0.61,2)
								SB0->B0_PRVV   := ROUND((_cPrc)*0.62,2)
								SB0->B0_PRVW   := ROUND((_cPrc)*0.63,2)
								SB0->(MsUnLock())
						ELSE
								IF SB1->B1_ATUAPRC == "S"

									CONOUT("ATUALIZANDO PRECO DO PRODUTO "+_cCodProd+" "+NoAcento(PadR(oJson['data']['descricao'],TamSX3("B1_DESC")[1])))
									RecLock("SB0",.F.)
									SB0->B0_PRV1   := ROUND((_cPrc),2)
									SB0->B0_PRV2   := ROUND((_cPrc)*0.54,2)
									SB0->B0_PRV3   := ROUND((_cPrc)*0.57,2)
									SB0->B0_PRV4   := ROUND((_cPrc)*0.585,2)
									SB0->B0_PRV5   := ROUND((_cPrc)*0.60,2)
									SB0->B0_PRV6   := ROUND((_cPrc)*0.40,2)
									SB0->B0_PRV7   := ROUND((_cPrc)*0.45,2)
									SB0->B0_PRV8   := ROUND((_cPrc)*0.60,2)
									SB0->B0_PRV9   := ROUND((_cPrc)-((_cPrc)*0.8165),2)
									SB0->B0_PRVA   := ROUND((_cPrc)/5.16,2)
									SB0->B0_PRVE   := ROUND((_cPrc)*1.11,2)
									SB0->B0_PRVU   := ROUND((_cPrc)*0.61,2)
									SB0->B0_PRVV   := ROUND((_cPrc)*0.62,2)
									SB0->B0_PRVW   := ROUND((_cPrc)*0.63,2)
									SB0->(MsUnLock())
								ENDIF


						endif
					ENDIF
					RecLock("SB1",.F.)
						SB1->B1_CUSTD  := SB0->B0_PRV9
					SB1->(MsUnLock())
					SB4->(DbSetOrder(1))
					IF !SB4->(DbSeek(xFilial("SB4")+_cCodProd))
						RecLock("SB4",.T.)
						SB4->B4_COD    := _cCodProd
						SB4->B4_DESC   := Upper(NoAcento(PadR(oJson['data']['descricao'],TamSX3("B1_DESC")[1])))
						SB4->B4_01CAT1 := ""
						SB4->B4_01CAT2 := ""
						SB4->B4_01CAT3 := ""
						SB4->B4_01CAT4 := ""
						SB4->B4_01CAT5 := ""
						SB4->B4_01CODMA:= ""
						SB4->B4_01COLEC:= "000003"
						SB4->B4_01UTGRD:= 'N'
						SB4->B4_01DTCAD:= dDatabase
						IF SB4->B4_DESC $ 'BRINCO'
							SB4->B4_UM     := "P"
						ELSE
							SB4->B4_UM     := "UN"
						ENDIF
						SB4->B4_TIPO   := "ME"
						SB4->B4_GRUPO  := ""
						SB4->B4_POSIPI := '71132000'
						SB4->B4_ORIGEM := '0'
						SB4->B4_LOCPAD := '01'
						SB4->B4_PRV1   := oJson['data']['precoEtiqueta']
						SB4->B4_STATUS := 'A'
						SB4->B4_PROC   := '049428'
						SB4->B4_LOJPROC:= '01'
						SB4->B4_CEST   := '2803700'
						SB4->B4_TE	   := '001'
						SB4->B4_TS	   := '501'
						SB4->(MsUnLock())
					ELSE
						RecLock("SB4",.F.)
						_cCod := PadR(oJson['data']['codigoFamilia'],TamSX3("AY0_CDNET")[1])
						AY0->(DbSetOrder(4))
						if AY0->(DbSeek(xFilial("AY0")+'1'+_cCod))
							SB4->B4_01CAT1 := AY0->AY0_CODIGO
						Endif
						_cCod := PadR(oJson['data']['codigoCor'],TamSX3("AY0_CDNET")[1])
						AY0->(DbSetOrder(4))
						if AY0->(DbSeek(xFilial("AY0")+'2'+_cCod))
							SB4->B4_01CAT2 := AY0->AY0_CODIGO
						Endif
						_cCod := PadR(oJson['data']['codigoTema'],TamSX3("AY0_CDNET")[1])
						AY0->(DbSetOrder(4))
						if AY0->(DbSeek(xFilial("AY0")+'3'+_cCod))
							SB4->B4_01CAT3 := AY0->AY0_CODIGO
						Endif
						_cCod := PadR(oJson['data']['codigoEstilo'],TamSX3("AY0_CDNET")[1])
						AY0->(DbSetOrder(4))
						if AY0->(DbSeek(xFilial("AY0")+'4'+_cCod))
							SB4->B4_01CAT4 := AY0->AY0_CODIGO
						Endif
						_cCod := PadR(oJson['data']['codigoFormato'],TamSX3("AY0_CDNET")[1])
						AY0->(DbSetOrder(4))
						if AY0->(DbSeek(xFilial("AY0")+'5'+_cCod))
							SB4->B4_01CAT5 := AY0->AY0_CODIGO
						Endif

						_cCod := PadR(oJson['data']['codigoCampanha'],TamSX3("AY2_CDNET")[1])
						//FILIAL+CODIGOCDNET
						AY2->(DbSetOrder(3))
						if AY2->(DbSeek(xFilial("AY2")+_cCod))
							SB4->B4_01CODMA:= AY2->AY2_CODIGO
						endif

						SB4->(MsUnLock())
					ENDIF
						
				endif
			Next nIt
			::SetResponse(oJson:toJSON())

		ENDIF
	ENDIF

	FreeObj(oJson)

	RestArea(aArea)
Return(.T.)



Static Function cadGrupo(_cCod,_cDesc)
	Local _cdNew := ""

	_cCod := PadR(_cCod,TamSX3("BM_CDNET")[1])
	_cDesc := PadR(_cDesc,TamSX3("BM_DESC")[1])

	dbSelectArea("SBM")
	SBM->(DbSetOrder(3))
	if !SBM->(DbSeek(xFilial("SBM")+_cCod))
		CONOUT("CADASTRANDO GRUPO "+NoAcento(_cDesc))

		_cGruPro := GETSXENUM("SBM","BM_GRUPO")
		CONFIRMSX8()
		RecLock("SBM",.T.)
		SBM->BM_FILIAL := xFilial("SBM")
		SBM->BM_GRUPO  := _cGruPro
		SBM->BM_DESC   := Upper(NoAcento(_cDesc))
		SBM->BM_CDNET  := _cCod
		SBM->(MsUnLock())   
	endif

Return _cdNew

Static Function cadCamp(_cCod,_cDesc)
	Local _cdNew := ""

	_cCod := PadR(_cCod,TamSX3("AY2_CDNET")[1])
	_cDesc := PadR(_cDesc,TamSX3("AY2_DESCR")[1])

	//FILIAL+CODIGOCDNET
	dbSelectArea("AY2")
	AY2->(DbSetOrder(3))
	if !AY2->(DbSeek(xFilial("AY2")+_cCod))
		CONOUT("CADASTRANDO CAMPANHA "+NoAcento(_cDesc))
		_cdNew := GetSxENum("AY2","AY2_CODIGO")
		Confirmsx8()
		RecLock("AY2",.T.)
		AY2->AY2_FILIAL := xFilial("AY2")
		AY2->AY2_CODIGO := _cdNew
		AY2->AY2_DESCR  := Upper(NoAcento(_cDesc))
		AY2->AY2_STATUS := '1'
		AY2->AY2_CDNET  := _cCod

		AY2->(MsUnLock())
	else
		_cdNew := AY2->AY2_CODIGO
	endif

Return _cdNew


Static Function cadFamilia(_cCod,_cDesc)
	Local _cdNew := ""

	_cCod := PadR(_cCod,TamSX3("AY0_CDNET")[1])
	_cDesc := PadR(_cDesc,TamSX3("AY0_DESC")[1])

	//FILIAL+TIPO+CODIGOCDNET
	AY0->(DbSetOrder(4))
	if !AY0->(DbSeek(xFilial("AY0")+'1'+_cCod))
		CONOUT("CADASTRANDO FAMILIA "+NoAcento(_cDesc))
		_cdNew := GETSXENUM("AY0","AY0_CODIGO")
		CONFIRMSX8()

		RecLock("AY0",.T.)
		AY0->AY0_FILIAL := xFilial("AY0")
		AY0->AY0_CODIGO := _cdNew
		AY0->AY0_DESC   := Upper(NoAcento(_cDesc))
		AY0->AY0_TIPO   := '1'
		AY0->AY0_STATUS := '1'
		AY0->AY0_CDNET  := _cCod
		AY0->(MsUnLock())
	else
		_cdNew := AY0->AY0_CODIGO
	endif

Return _cdNew


Static Function cadCor(_cCod,_cDesc)
	Local _cdNew := ""

	_cCod := PadR(_cCod,TamSX3("AY0_CDNET")[1])
	_cDesc := PadR(_cDesc,TamSX3("AY0_DESC")[1])

	//FILIAL+TIPO+CODIGOCDNET
	AY0->(DbSetOrder(4))
	if !AY0->(DbSeek(xFilial("AY0")+'2'+_cCod))
		CONOUT("CADASTRANDO COR "+NoAcento(_cDesc))
		_cdNew := GETSXENUM("AY0","AY0_CODIGO")
		CONFIRMSX8()

		RecLock("AY0",.T.)
		AY0->AY0_FILIAL := xFilial("AY0")
		AY0->AY0_CODIGO := _cdNew
		AY0->AY0_DESC   := Upper(NoAcento(_cDesc))
		AY0->AY0_TIPO   := '2'
		AY0->AY0_STATUS := '1'
		AY0->AY0_CDNET  := _cCod
		AY0->(MsUnLock())
	else
		_cdNew := AY0->AY0_CODIGO
	endif

Return _cdNew


Static Function cadTema(_cCod,_cDesc)
	Local _cdNew := ""

	_cCod := PadR(_cCod,TamSX3("AY0_CDNET")[1])
	_cDesc := PadR(_cDesc,TamSX3("AY0_DESC")[1])

	//FILIAL+TIPO+CODIGOCDNET
	AY0->(DbSetOrder(4))
	if !AY0->(DbSeek(xFilial("AY0")+'3'+_cCod))
		CONOUT("CADASTRANDO TEMA "+NoAcento(_cDesc))
		_cdNew := GETSXENUM("AY0","AY0_CODIGO")
		CONFIRMSX8()

		RecLock("AY0",.T.)
		AY0->AY0_FILIAL := xFilial("AY0")
		AY0->AY0_CODIGO := _cdNew
		AY0->AY0_DESC   := Upper(NoAcento(_cDesc))
		AY0->AY0_TIPO   := '3'
		AY0->AY0_STATUS := '1'
		AY0->AY0_CDNET  := _cCod
		AY0->(MsUnLock())
	else
		_cdNew := AY0->AY0_CODIGO
	endif

Return _cdNew



Static Function cadEstilo(_cCod,_cDesc)
	Local _cdNew := ""

	_cCod := PadR(_cCod,TamSX3("AY0_CDNET")[1])
	_cDesc := PadR(_cDesc,TamSX3("AY0_DESC")[1])

	//FILIAL+TIPO+CODIGOCDNET
	AY0->(DbSetOrder(4))
	if !AY0->(DbSeek(xFilial("AY0")+'4'+_cCod))
		CONOUT("CADASTRANDO ESTILO "+NoAcento(_cDesc))
		_cdNew := GETSXENUM("AY0","AY0_CODIGO")
		CONFIRMSX8()

		RecLock("AY0",.T.)
		AY0->AY0_FILIAL := xFilial("AY0")
		AY0->AY0_CODIGO := _cdNew
		AY0->AY0_DESC   := Upper(NoAcento(_cDesc))
		AY0->AY0_TIPO   := '4'
		AY0->AY0_STATUS := '1'
		AY0->AY0_CDNET  := _cCod
		AY0->(MsUnLock())
	else
		_cdNew := AY0->AY0_CODIGO
	endif

Return _cdNew

Static Function cadFormato(_cCod,_cDesc)
	Local _cdNew := ""

	_cCod := PadR(_cCod,TamSX3("AY0_CDNET")[1])
	_cDesc := PadR(_cDesc,TamSX3("AY0_DESC")[1])

	//FILIAL+TIPO+CODIGOCDNET
	AY0->(DbSetOrder(4))
	if !AY0->(DbSeek(xFilial("AY0")+'5'+_cCod))
		CONOUT("CADASTRANDO FORMATO "+NoAcento(_cDesc))
		_cdNew := GETSXENUM("AY0","AY0_CODIGO")
		CONFIRMSX8()

		RecLock("AY0",.T.)
		AY0->AY0_FILIAL := xFilial("AY0")
		AY0->AY0_CODIGO := _cdNew
		AY0->AY0_DESC   := Upper(NoAcento(_cDesc))
		AY0->AY0_TIPO   := '5'
		AY0->AY0_STATUS := '1'
		AY0->AY0_CDNET  := _cCod
		AY0->(MsUnLock())
	else
		_cdNew := AY0->AY0_CODIGO
	endif

Return _cdNew


Static Function cadAcessorio(_cCod,_cDesc)
	Local _cdNew := ""

	_cCod := PadR(_cCod,TamSX3("AY0_CDNET")[1])
	_cDesc := PadR(_cDesc,TamSX3("AY0_DESC")[1])

	//FILIAL+TIPO+CODIGOCDNET
	AY0->(DbSetOrder(4))
	if !AY0->(DbSeek(xFilial("AY0")+'6'+_cCod))
		CONOUT("CADASTRANDO ACESSORIO "+NoAcento(_cDesc))
		_cdNew := GETSXENUM("AY0","AY0_CODIGO")
		CONFIRMSX8()

		RecLock("AY0",.T.)
		AY0->AY0_FILIAL := xFilial("AY0")
		AY0->AY0_CODIGO := _cdNew
		AY0->AY0_DESC   := Upper(NoAcento(_cDesc))
		AY0->AY0_TIPO   := '6'
		AY0->AY0_STATUS := '1'
		AY0->AY0_CDNET  := _cCod
		AY0->(MsUnLock())
	else
		_cdNew := AY0->AY0_CODIGO
	endif

Return _cdNew





static FUNCTION NoAcento(cString)
Local cChar  := ""
Local nX     := 0
Local nY     := 0
Local cVogal := "aeiouAEIOU"
Local cAgudo := "áéíóú"+"ÁÉÍÓÚ"
Local cCircu := "âêîôû"+"ÂÊÎÔÛ"
Local cTrema := "äëïöü"+"ÄËÏÖÜ"
Local cCrase := "àèìòù"+"ÀÈÌÒÙ"
Local cTio   := "ãõÃÕ"
Local cCecid := "çÇ"
Local cMaior := "&lt;"
Local cMenor := "&gt;"

For nX:= 1 To Len(cString)
	cChar:=SubStr(cString, nX, 1)
	IF cChar$cAgudo+cCircu+cTrema+cCecid+cTio+cCrase
		nY:= At(cChar,cAgudo)
		If nY > 0
			cString := StrTran(cString,cChar,SubStr(cVogal,nY,1))
		EndIf
		nY:= At(cChar,cCircu)
		If nY > 0
			cString := StrTran(cString,cChar,SubStr(cVogal,nY,1))
		EndIf
		nY:= At(cChar,cTrema)
		If nY > 0
			cString := StrTran(cString,cChar,SubStr(cVogal,nY,1))
		EndIf
		nY:= At(cChar,cCrase)
		If nY > 0
			cString := StrTran(cString,cChar,SubStr(cVogal,nY,1))
		EndIf
		nY:= At(cChar,cTio)
		If nY > 0
			cString := StrTran(cString,cChar,SubStr("aoAO",nY,1))
		EndIf
		nY:= At(cChar,cCecid)
		If nY > 0
			cString := StrTran(cString,cChar,SubStr("cC",nY,1))
		EndIf
	Endif
Next

If cMaior$ cString
	cString := strTran( cString, cMaior, "" )
EndIf
If cMenor$ cString
	cString := strTran( cString, cMenor, "" )
EndIf

For nX:=1 To Len(cString)
	cChar:=SubStr(cString, nX, 1)
	If (Asc(cChar) < 32 .Or. Asc(cChar) > 123) .and. !cChar $ '|'
		cString:=StrTran(cString,cChar,".")
	Endif
Next nX
Return cString
