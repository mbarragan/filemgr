import java.util.Arrays;
import dev.quercusdata.indexer.FileIndexer;
import dev.quercusdata.indexer.Parameters;
public class SmokeRun {
    public static void main(String[] args) {
        Parameters p = new Parameters();
        p.setUnidadOrigen(args[0]);
        p.setFicheroSalida("indice.csv");
        p.setExcluyeDirectorios(Arrays.asList());
        p.setExtensionesFicheros(Arrays.asList("mp4","avi"));
        new FileIndexer(p);
    }
}
