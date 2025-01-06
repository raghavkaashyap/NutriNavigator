//package coms309.Recipe;
//
//import jakarta.transaction.Transactional;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface RecipeRepository extends JpaRepository<Recipe, Long> {
//    //Exists by methods
//    boolean existsByName(String name);
//
//    //Find by methods
//    Recipe findById(int id);
//    Recipe findByName(String name);
//
//    //Delete methods
//    @Transactional
//    void deleteByName(String name);
//    @Transactional
//    void deleteById(int id);
//}
