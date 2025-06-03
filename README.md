# 🚀  Project Successfully Deployed: Spring Boot Backend Application Exposing REST APIs on Kubernetes (Minikube)



🔧 Technologies Used:
1- Backend: Spring Boot 3.4.5 (Java 17)
2- Database: MariaDB 11
3- Orchestration & Deployment: Kubernetes (Minikube)
4- Containerization: Docker
5- API Documentation: Swagger / OpenAPI
6- Scalability: HorizontalPodAutoscaler (auto-scaling backend pods)

🎯 Context & Goal:
Deployment of a RESTful backend application built with Spring Boot, exposing endpoints to manage Client, Product, and AchatProduct entities. These APIs allow creating, retrieving, and managing client, product, and purchase data. The backend is containerized and deployed in a local Kubernetes cluster (Minikube), with MariaDB as the persistent database.

⚙️ Key Features:
1- REST API exposure for client, product, and purchase data
2- Automatic data initialization (clients, products, purchases) at application startup
3- Integrated Swagger UI documentation for easy endpoint testing via /swagger-ui/index.html
4- Horizontal scaling configured to scale up to 5 backend pods based on load via HorizontalPodAutoscaler
5- Pod monitoring and management via kubectl and the Kubernetes Dashboard

🧠 What I Learned:
1- Full backend deployment with Spring Boot on Kubernetes
2- Managing a MariaDB database within a Kubernetes cluster
3- The importance of ConfigMaps, Secrets, and environment variables for secure configuration
4- Using Rolling Update strategy (kubectl set image) to update an image without service interruption
5- Automatic image update without needing a rollout restart
6- Debugging common deployment errors like ImagePullBackOff, CrashLoopBackOff
7- Configuring automatic scaling with HorizontalPodAutoscaler

#Etapes de mise en place 
Voici toutes les **étapes techniques détaillées** pour mettre en place un déploiement complet d’une application Spring Boot sur Kubernetes (via Minikube), avec Docker, configuration des manifests YAML et scalabilité.

---

## 🧱 1. **Développement de l’application Spring Boot**

### ✅ Objectif :

Créer un backend REST exposant des endpoints sur les entités : `Client`, `Product`, `AchatProduct`.

### 📁 Étapes :

* Créer les entités JPA et leurs relations.
* Créer les `Repository`, `Service`, `Controller`.
* Ajouter Swagger (`springdoc-openapi-starter-webmvc-ui`).
* Créer une classe `@Configuration` pour charger les données (10 clients, 20 produits, 50 achats).

---

## 🐳 2. **Containerisation avec Docker**

### ✅ Objectif :

Créer une image Docker exécutable du projet Spring Boot.

### 📁 Étapes :

**a. Créer un `Dockerfile` :**

```Dockerfile
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**b. Générer l’image Docker :**

```bash
./mvnw clean package -DskipTests
docker build -t migration-app:latest .
```

---

## 🚀 3. **Installation de Minikube**

```bash
minikube start --driver=docker
```

---

## ⚙️ 4. **Déploiement de MariaDB sur Kubernetes**

### ✅ Objectif :

Déployer une base de données MariaDB persistante.

### 📁 Étapes :

**a. ConfigMap & Secret (si besoin) :**

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: mariadb-secret
type: Opaque
data:
  password: cm9vdA==  # base64("root")
```

**b. Deployment YAML :**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mariadb-deployment
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mariadb
  template:
    metadata:
      labels:
        app: mariadb
    spec:
      containers:
        - name: mariadb
          image: mariadb:11
          env:
            - name: MARIADB_ROOT_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: mariadb-secret
                  key: password
          ports:
            - containerPort: 3306
          volumeMounts:
            - mountPath: /var/lib/mysql
              name: mariadb-storage
      volumes:
        - name: mariadb-storage
          emptyDir: {}
```

**c. Service YAML :**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: mariadb
spec:
  selector:
    app: mariadb
  ports:
    - port: 3306
      targetPort: 3306
```

---

## 🧩 5. **Déploiement de l’application Spring Boot sur Kubernetes**

**a. Créer une image et la rendre accessible à Minikube :**

```bash
eval $(minikube docker-env)
docker build -t migration-app:latest .
```

**b. Créer `deployment.yaml` :**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: migration-deployment
spec:
  replicas: 2
  selector:
    matchLabels:
      app: migration
  template:
    metadata:
      labels:
        app: migration
    spec:
      containers:
        - name: migration
          image: migration-app:latest
          ports:
            - containerPort: 8080
          env:
            - name: SPRING_DATASOURCE_URL
              value: jdbc:mariadb://mariadb:3306/mydb
            - name: SPRING_DATASOURCE_USERNAME
              value: root
            - name: SPRING_DATASOURCE_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: mariadb-secret
                  key: password
```

**c. Créer `service.yaml` :**

```yaml
apiVersion: v1
kind: Service
metadata:
  name: migration-service
spec:
  type: NodePort
  selector:
    app: migration
  ports:
    - port: 8080
      targetPort: 8080
      nodePort: 30080
```

---

## 📈 6. **Ajout de l’autoscaling (HPA)**

```yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: migration-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: migration-deployment
  minReplicas: 2
  maxReplicas: 5
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 50
```

---

## 🌀 7. **Stratégie de Rolling Update**

Déjà gérée par défaut dans le `Deployment` :

```yaml
strategy:
  type: RollingUpdate
  rollingUpdate:
    maxUnavailable: 1
    maxSurge: 1
```

**Pour mettre à jour une image sans downtime :**

```bash
kubectl set image deployment/migration-deployment migration=migration-app:v2 -n dev
```

---

## 📄 8. **Accès Swagger UI**

Swagger disponible à :

```bash
http://localhost:30080/swagger-ui/index.html
```

---

## 📊 9. **Monitoring et Debug**

### 📌 Vérifier les pods :

```bash
kubectl get pods -n dev
```

### 📌 Voir les logs :

```bash
kubectl logs migration-deployment-xxxxx -n dev
```

### 📌 Accès au Kubernetes Dashboard :

```bash
minikube dashboard
```

---

## ✅ Résumé des commandes utiles

| Commande                                                     | Description                            |
| ------------------------------------------------------------ | -------------------------------------- |
| `kubectl apply -f .`                                         | Appliquer tous les manifests           |
| `kubectl set image ...`                                      | Mettre à jour l’image (rolling update) |
| `kubectl rollout restart deployment/migration-deployment`    | Redémarrer manuellement                |
| `kubectl scale deployment migration-deployment --replicas=4` | Changer le nombre de pods              |
| `kubectl get hpa`                                            | Voir l’état de l’autoscaler            |
| `minikube service migration-service`                         | Ouvre l’application dans le navigateur |

---

Souhaitez-vous que je génère automatiquement tous les fichiers YAML ou un dépôt GitHub avec cette architecture prête à l’emploi ?
