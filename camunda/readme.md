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