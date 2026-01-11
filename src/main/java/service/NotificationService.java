package service;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationService {
    private final EmpruntService service = new EmpruntService();
    public List<String> getNotifications() {
        return service.getEmpruntsEnRetard().stream()
                .map(e -> "⚠ " + e.getUtilisateur().getNom() + " → " + e.getLivre().getTitre())
                .collect(Collectors.toList());
    }
}