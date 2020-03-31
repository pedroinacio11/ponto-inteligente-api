package com.phinacio.pontointeligente.api.repositories;

import com.phinacio.pontointeligente.api.entities.Empresa;
import com.phinacio.pontointeligente.api.entities.Funcionario;
import com.phinacio.pontointeligente.api.entities.Lancamento;
import com.phinacio.pontointeligente.api.enums.PerfilEnum;
import com.phinacio.pontointeligente.api.enums.TipoEnum;
import com.phinacio.pontointeligente.api.utils.PasswordUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    private Long funcionarioId;

    @Before
    public void setUp() throws Exception {
        Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());

        Funcionario funcionario = this.funcionarioRepository.save(obterDadosFuncionario(empresa));
        this.funcionarioId = funcionario.getId();

        this.lancamentoRepository.save(obterDadosLancamentos(funcionario));
        this.lancamentoRepository.save(obterDadosLancamentos(funcionario));

    }

    @After
    public void tearDown() throws Exception {
        this.empresaRepository.deleteAll();
    }

    @Test
    public void testBuscarLancamentosPorFuncionarioId() {
        List<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId);

        Assert.assertEquals(2, lancamentos.size());
    }

    @Test
    public void testBuscarLancamentosPorFuncionarioIdPaginado() {

        Page<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId, PageRequest.of(0,10));
        Assert.assertEquals(2, lancamentos.getTotalElements());

    }

    private Lancamento obterDadosLancamentos(Funcionario funcionario){
        Lancamento lancamento = new Lancamento();
        lancamento.setData(new Date());
        lancamento.setTipo(TipoEnum.INICIO_ALMOCO);
        lancamento.setFuncionario(funcionario);
        return lancamento;
    }

    private Funcionario obterDadosFuncionario(Empresa empresa) throws NoSuchAlgorithmException {

        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Pedro Henrique");
        funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
        funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
        funcionario.setCpf("40792980809");
        funcionario.setEmail("pedro.inacio11@gmail.com");
        funcionario.setEmpresa(empresa);
        return funcionario;

    }

    private Empresa obterDadosEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial("Empresa de Teste");
        empresa.setCnpj("415463251000122");
        return empresa;
    }

}
