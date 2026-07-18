import dao.ProductDAO;
import dao.impl.ProductDAOImpl;
import service.ProductService;
import service.impl.ProductServiceImpl;

public class Main {
    public static void main(String[] args) {

        ProductDAO dao = new ProductDAOImpl();
        ProductService service = new ProductServiceImpl(dao);

        System.out.println("Is 'Danish Pastry' existing? : " + dao.existsByName("Danish Pastry"));
        System.out.println("Is 'Danish Pastry' existing? : " + dao.existsByName("TESTING"));

//        System.out.println("======= FindAll() =======");
//        List<ProductDTO> allProds = service.findAll();
//        for (ProductDTO p : allProds) {
//            System.out.println(p);
//        }
//        System.out.println("=========================");
//
//        System.out.println("======= FindById() =======");
//        Optional<ProductDTO> foundProd = service.findById(2L);
//        foundProd.ifPresent(System.out::println);
//        System.out.println("=========================");
//
//        System.out.println("======= FindPopular() =======");
//        List<ProductDTO> popularProds = service.findPopular();
//        for (ProductDTO p : popularProds) {
//            System.out.println(p);
//        }
//        System.out.println("=========================");
//
//        System.out.println("======= FindByCategory() =======");
//        List<ProductDTO> catProds = service.findByCategory("bread");
//        for (ProductDTO p : catProds) {
//            System.out.println(p);
//        }
//        System.out.println("=========================");
//
//        System.out.println("======= SEARCH() =======");
//        List<ProductDTO> searchProds = service.search("cake");
//        for (ProductDTO p : searchProds) {
//            System.out.println(p);
//        }
//        System.out.println("=========================");
    }
}
