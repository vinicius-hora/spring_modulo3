package curso.springboot.springboot.service;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Component
public class ReportUtil implements Serializable {
	
	private static final long serialVersionUID = 1L;

	//retorna o PDF em byute para download no navegador
	public byte[] gerarRelatorio(List listDados, String relatorio, ServletContext servletContext)
	throws Exception{
		//cria a lista de dados para o relatório com a lista de objetos para impressao
		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listDados);
		
		//carrega o caminho do arquivo jasper
		String caminhoJasper = servletContext.getRealPath("relatorios") + File.separator
				+ relatorio + ".jasper";
		
		//carrega o arquivo jasper passando os dados
		JasperPrint impressoraJasper = JasperFillManager
				.fillReport(caminhoJasper, new HashMap<>(), jrbcds);
		
		//exporta para byte para fazer donwnload do PDF
		return JasperExportManager.exportReportToPdf(impressoraJasper);
	}

}
