package br.com.fenix.teste;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.List;
//import net.sf.ofx4j.domain.data.MessageSetType;
//import net.sf.ofx4j.domain.data.ResponseEnvelope;
//import net.sf.ofx4j.domain.data.ResponseMessageSet;
//import net.sf.ofx4j.domain.data.banking.BankStatementResponse;
//import net.sf.ofx4j.domain.data.banking.BankStatementResponseTransaction;
//import net.sf.ofx4j.domain.data.banking.BankingResponseMessageSet;
//import net.sf.ofx4j.domain.data.common.Transaction;
//import net.sf.ofx4j.domain.data.signon.SignonResponse;
//import net.sf.ofx4j.io.AggregateUnmarshaller;
//import net.sf.ofx4j.io.OFXParseException;

public class TesteLerOfx {
//	public class Main {
//
//		public static void main(String[] args) throws IOException, OFXParseException {
//		   AggregateUnmarshaller a = new AggregateUnmarshaller(ResponseEnvelope.class);
//		   ResponseEnvelope re = (ResponseEnvelope) a.unmarshal(new FileInputStream(new File(System.getProperty("user.home") + "/Downloads/Temp/extrato.ofx")));
//
//		   //objeto contendo informações como instituição financeira, idioma, data da conta.
//		   SignonResponse sr = re.getSignonResponse();
//
//		   //como não existe esse get "BankStatementResponse bsr = re.getBankStatementResponse();"
//		   //fiz esse codigo para capturar a lista de transações
//		   MessageSetType type = MessageSetType.banking;
//		   ResponseMessageSet message = re.getMessageSet(type);
//
//		   if (message != null) {
//		       List<BankStatementResponseTransaction> bank = ((BankingResponseMessageSet) message).getStatementResponses();
//		       for (BankStatementResponseTransaction b : bank) {
//		           System.out.println("cc: " + b.getMessage().getAccount().getAccountNumber());
//		           System.out.println("ag: " + b.getMessage().getAccount().getBranchId());
//		           System.out.println("balanço final: " + b.getMessage().getLedgerBalance().getAmount());
//		           System.out.println("dataDoArquivo: " + b.getMessage().getLedgerBalance().getAsOfDate());
//		           List<Transaction> list = b.getMessage().getTransactionList().getTransactions();
//		           System.out.println("TRANSAÇÕES\n");
//		           for (Transaction transaction : list) {
//		               System.out.println("tipo: " + transaction.getTransactionType().name());
//		               System.out.println("id: " + transaction.getId());
//		               System.out.println("data: " + transaction.getDatePosted());
//		               System.out.println("valor: " + transaction.getAmount());
//		               System.out.println("descricao: " + transaction.getMemo());
//		               System.out.println("");
//		           }
//		       }
//		   }
//		}
//	}
}
