Explizite Trennung von Prozessstart und reinreichen des Events zwischen Dispatcher und Eventlayer.
- Es gibt unterschiedliche Wege i Camunda eine Prozessinstanz zu starten, (explizit, Message, Event o.ä.). Aber in jedem Fall muss der Dispatcher vorher geprüft haben, ob zu einem Event eine PI existiert oder existieren kann.
- Trennung von PI-Start und Daten-Einreichung erlaubt imho eine etwas sauberere Modellierung, weil der Dispatcher keine tiefere Kenntnis von der Bedeutung eines Events haben muss (ausser der Korrelation)
- Das erste Event kann im Prozess genauso gehandhabt werden wie alle anderen Events.

# Bei geänderter Reihenfolge der Events gibt es einige Dinge zu berücksichtigen.

## Szenarien
### Szenario 1:  Alle Events in der richtigen Reihenfolge.
### Szenario 2:  Mittlere Events sind vertauscht, Vertauschung ist nicht relevant.
### Szenario 3:  Mittlere Events sind vertauscht, Vertauschung nicht relevant.
### Szenario 4:  Mittlere Events sind vertauscht, Vertauschung relevant.
### Szenario 5:  Mittlere Events sind vertauscht, Event bleibt aus.
### Szenario 6:  Abschlussevent kommt, vor mittleren Events, nicht relevant
### Szenario 7:  Abschlussevent kommt, vor mittleren Events, relevant

# Nützliche Links
https://forum.camunda.org/t/set-local-variables-in-event-subprocess-via-rest-api/13157/8
https://forum.camunda.org/t/message-buffer-plugin/20456/14


# Lösungsvarianten:
## 1. Reihenfolge der Events erzwingen
Prozess mit eingbettem Event-Subprozess, Kommunikation über Signals
- Prozessstart und einreichen der Events werden voneinander getrennt.
- Sofern die PI lebt, können über eingehenden Nachrichten Subprozesse gestartet werden, die als Cache dienen
- Alle eingehenden Events werden einem Prozesschritt zugeordnet.
- In Intervallen wird geprüft, in welchem Wait-State sich der Prozess gerabe befindet.
- Passen eingehendens Event und Wait-State zueinander, wird die PI per Signal über das Event informiert 
### Probleme:
- Signal ist ein Broadcast-Event, sofern keine weiteren Massnahmen ergriffen werden (executionContext bestimmen), werden ALLE PIs über das Event erzeugt. die Ermittlung des richtigen ExecutionContexts ist nicht trivial, zumal der EC nicht statisch ist und Instanzen auch in neue ECs verschoben werden können. Muss also jeweils zeitnah sein. Mit der REST API sehr aufwändig.
- Es muss sichergestellt werden, dass ALLE Variablen lokal für den Subprozess sind, da es sonst zu Race-Conditions kommen kann. Hier gibt es mit der REST Api erhebliche Probleme und auch bei der Verwendung des Java-API warten Fallstricke.
- Ausbleibende Events blockieren den Prozess dauerhaft.

### Vorteile
-Der Prozess selber muss tendentiell wenig Eskalationslogik enthalten, da diese eher im Subprozess angesiedelt ist.
-Der Prozess ist wirklich kontinuierlich und sieht aus, wie man sich den Fachprozess naiv vorstellt.



Links funktioniere nicht, da sich Links nur innerhalb des gleichen ExecutionContexts sehen. SubProzess<> Hauptprozess funktionieren nicht. Implementierung über zwei getrennte PIs fällt damit auch aus.

Fachprozess und Warteprozess werden getrennt: Löst einige der Probleme die die Verwendung eines Subprozesses mitbringen, ist aber eine zusätzliche Abstarktion und macht das Bild uübersichtlicher
1. PI für FP, 0..n gleichzeizige Warte PIs für die Events.