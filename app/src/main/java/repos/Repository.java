package repos;

import java.util.List;

public interface Repository<T> {
    List<T> getAll();
    List<T> getByName(String name);
}
