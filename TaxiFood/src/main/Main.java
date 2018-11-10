package main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import matcher.Matcher;
import matcher.MatcherImpl;
import model.Car;
import model.Driver;
import model.Localization;
import model.Passenger;
import model.PaymentType;
import pember.kmeans.geo.Cluster;
import pember.kmeans.geo.ClusterService;
import pember.kmeans.geo.GeographicPoint;
import rules.ChainedRestriction;
import rules.Restriction;
import rules.Restriction.StandardRestriction;
import service.Service;
import utils.CSV;

public class Main {

	private static final long serialVersionUID = -5680964038598774462L;
	private static final Logger logger = Logger.getLogger(Main.class);
	private static final int NUMBER_OF_TAXI = 10;
	private static final int NUMBER_OF_PASSENGER = 100;
	private static final int LATITUDE = 90;
	private static final int LONGITUDE = 180;
	private static final int K = 4;
	private static ArrayList<Localization> centroids;
	static String log4jConfPath = "C:\\Users\\natha\\eclipse-workspace\\TaxiFood\\log4j.properties.txt";
	
    public static void main(final String[] args) throws InterruptedException {
    	PropertyConfigurator.configure(log4jConfPath);
    	
    	
    	/* - Cada pedido de taxi tem sua origem, com isso são constituidos de uma latitude e longitude.
    	 * - Por meio de um historico de pedidos de taxi podemos dividir uma cidade em partes de forma a melhor distribuir os taxis.
    	 * - Com o kmeans podemos descobrir os centros desses pedidos e então dividir em subgrupos, de acordo com a aproximacao do centro.
    	 * - Vale recaltar que usamos a distancia de haversine entre os pontos, pois esta forma de medir distancia eh a mais correta 
    	 *   devido a terra nao ser plana.
    	 * - A parte de kmeansProcess nao deveria acontecer aqui. Apenas por ser uma simulacao ele eh chamado neste local.  
    	 * */
        
    	centroids = kmeansProcess(K);

     	
		final Restriction restriction = new ChainedRestriction(Arrays.asList(StandardRestriction.DRIVER_STATUS,
				StandardRestriction.PASSENGER_STATUS,  StandardRestriction.PAYMENT, StandardRestriction.SMOKING ));

		/* - Como estamos criando cluster, motoristas e passageiross do mesmo cluster jah tem uma distancia pequena entre eles.
		   - Por isso não minizaremos a distancia, pois a clusterizacao jah eh responsavel por isso.
		  */
		//final Selector selector = StandardSelector.MINIMIZE_DISTANCE;

		final Matcher matcher = new MatcherImpl(restriction);

		final Service simulator = new Service(matcher, K);
    	
    	BigDecimal x;
    	BigDecimal y; 
		int count = 0;
		int indexCentroid = 0;
		Localization localization; 
		Driver driver;
		Passenger passenger;
		Car car;

		while (NUMBER_OF_TAXI > count) {
			// Gera localizacao randomica
			x = BigDecimal.valueOf(Math.random()*LATITUDE);
			y = BigDecimal.valueOf(Math.random()*LONGITUDE);
			
			// Usar o centroid aqui, para que baseado na localizacao sabermos onde eh melhor colocar
			localization = new Localization(x,y);
			
			// Identifica qual centroid eh mais proximo da localizacao
			indexCentroid = SearchCluster(localization);
			
			// Carro foi usado pensando em se fazer uma interface real do problema
			car = generateCar();
			
			driver = generateDriver(localization , car);
			simulator.add(driver,indexCentroid);
			count++;
		}

		count = 0;
		while (NUMBER_OF_PASSENGER > count) {
			// Gera localizacao randomica
			x = BigDecimal.valueOf(Math.random()*LATITUDE);
			y = BigDecimal.valueOf(Math.random()*LONGITUDE);
			
			// Usar o centroid aqui, para que baseado na localizacao sabermos onde eh melhor colocar
			localization = new Localization(x,y);
			
			// Identifica qual centroid eh mais proximo da localizacao
			indexCentroid = SearchCluster(localization);
			
			passenger = generatePassenger(localization);
			
			simulator.add(passenger,indexCentroid);
			count++;
		}
		
		while(true) {}
    }

    public static ArrayList<Localization> kmeansProcess(int K) {
    	/* https://github.com/spember/geo-cluster
    	 * This small library is an implementation of KMeans clustering for geographical points, written in Groovy. 
    	 * It uses the Haversine Formula as its distance metric.

		 *	KMeans is both clever and powerful but it has two important things to think about: 
		 *1) which distance metric you use to calculate distance between points (e.g. x/y grid distance may not always be correct) and 
		 *2) how to optimize for k, the number of clusters. KMeans makes no assumption naively about your choice of k and it may take 
		 *several runs to find the 'correct' k value for your domain. For example, one may run this algrothim several times to find 
		 *the optimal k where no cluster has locations more than 100 kilometers away from the center.
    	 * 
    	
    	*/
    	/* Abre o historico */
    	CSV csvOrigins = new CSV();
    	List<Localization>origins = csvOrigins.readCSV();
    	
    	
    	List<GeographicPoint> points = new ArrayList<GeographicPoint>();
    	logger.info("Pontos de origem:");
    	for (Localization l : origins) {
        	logger.info(l.toString());
    		points.add(l);
    	}
    	
    	List<Cluster> resultCluster = new ArrayList<Cluster>();
    	resultCluster = ClusterService.cluster(points,K);
    	ArrayList<Localization> localizations = new ArrayList<Localization>();
    	logger.info("Resultados centroids:");
    	for (Cluster c : resultCluster) {
    		if (c != null) {
            	logger.info("Latitude=" + c.getLatitude());
            	logger.info("Longitude=" + c.getLongitude());
    			localizations.add(new Localization(c.getLatitude(), c.getLongitude()));
    		}
    	}
    	
    	return localizations;
    }
    
    public static int SearchCluster(Localization localization){
    	double distance = localization.distanceTo(centroids.get(0));
    	int K = 0;
    	logger.info("Buscando o cluster da localização:");
    	for (int i = 1; i < K; i++) {
    		if(distance > localization.distanceTo(centroids.get(i))){
    			distance = localization.distanceTo(centroids.get(i));
    			logger.info("distance=" + distance);
    			K = i;
    		}
    	}
    	return K;
    }
    
    public static Localization getCentroid(int index) {
    	return centroids.get(index);
    }
    
    public static Car generateCar() {
    	String [] models = {"gol","corsa","parati","fusca","fox"};
    	String [] cor = {"azul","vermelho","branco","rosa","verde"};
    	String licensePlat = String.valueOf(Math.random()*3000);
    	
		return new Car(licensePlat,models[(int)Math.random()*100 % models.length], cor[(int) (Math.random()*100 % cor.length)]);
    	
    }
    
    public static Driver generateDriver(Localization localization, Car car) {
    	ArrayList<PaymentType> paymentType = new ArrayList<PaymentType>();
		paymentType.add(PaymentType.CREDIT_CARD);
		paymentType.add(PaymentType.BITCOIN);
		paymentType.add(PaymentType.MONEY);
		
		String [] name= {"mario","joao","celso","maria","jose","ana"};
		String telephone =  String.valueOf((int)(Math.random() * 100) + 34635600);
		int nameIndex = ThreadLocalRandom.current().nextInt(0, name.length);
		return new Driver(name[nameIndex], telephone, localization , car, paymentType);
    }
    
    
    public static Passenger generatePassenger(Localization localization) {
    	PaymentType [] payment = {PaymentType.CREDIT_CARD, PaymentType.BITCOIN, PaymentType.MONEY};
		
		String [] name= {"mario","joao","celso","maria","jose","ana"};
		String telephone =   String.valueOf((int)(Math.random() * 100) + 34635600);
		int nameIndex = ThreadLocalRandom.current().nextInt(0, name.length);
		int paymentIndex = ThreadLocalRandom.current().nextInt(0, payment.length);
		
		return new Passenger(name[nameIndex], telephone, localization , payment[paymentIndex], new Random().nextBoolean());
    }

}
