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
Voici un exemple de **README** clair et structuré expliquant comment gérer le **build, le chargement et le déploiement d’une nouvelle image Docker** pour ton projet `migration-app` dans Minikube, en se basant sur les informations que tu as fournies :

---

# README – Mise à jour de l’image Docker `migration-app` dans Minikube

Ce guide explique comment **builder une nouvelle version** de l’application `migration-app`, la **charger dans Minikube**, et la **déployer avec Kubernetes**.

---

## 1️⃣ Build de la nouvelle image Docker

Lorsque tu modifies le code et que tu veux créer une nouvelle image :

```bash
# Depuis le répertoire du projet contenant le Dockerfile
docker build -t migration-app:newversion .
```

* `newversion` → remplace par la version souhaitée (ex : `v2`, `20251126`, etc.).
* Cette commande génère l’image Docker avec ton code à jour.

---

## 2️⃣ Charger l’image dans Minikube

Minikube utilise son propre Docker interne. Il faut donc **charger l’image dans Minikube** :

```bash
minikube image load migration-app:newversion
```

* Vérifie que l’image est bien présente dans Minikube :

```bash
minikube ssh
docker images | grep migration-app
```

* Tu devrais voir `migration-app  newversion` dans la liste.

---

## 3️⃣ Mettre à jour le déploiement Kubernetes

### Option 1 : Modifier directement le YAML

1. Ouvre `migration-deployment.yaml`.
2. Change la version de l’image :

```yaml
containers:
  - name: migration
    image: migration-app:newversion
```

3. Applique le manifeste :

```bash
kubectl apply -f migration-deployment.yaml -n dev
```

---

### Option 2 : Redéployer directement via `kubectl`

Si tu ne veux pas modifier le YAML, tu peux forcer Kubernetes à utiliser la nouvelle image **si `imagePullPolicy` est `IfNotPresent` ou `Always`** :

```bash
kubectl set image deployment/migration-deployment migration=migration-app:newversion -n dev
kubectl rollout status deployment/migration-deployment -n dev
```

Ou pour redémarrer le déploiement afin de relancer les pods avec la nouvelle image :

```bash
kubectl rollout restart deployment migration-deployment -n dev
```

---

## 4️⃣ Vérification

* Vérifie que les pods sont **running** avec la nouvelle image :

```bash
kubectl get pods -n dev -o wide
kubectl describe pod <pod-name> -n dev
```

* Vérifie l’image utilisée sur le déploiement :

```bash
kubectl get deployment migration-deployment -n dev -o=jsonpath='{.spec.template.spec.containers[*].image}'
```

* Suivi des logs :

```bash
kubectl logs -f <pod-name> -n dev
```

---

## ✅ Bonnes pratiques

1. **Versionne toujours l’image Docker** après modification du code (évite `latest`).
2. **Charge l’image dans Minikube** à chaque build pour être sûr que Kubernetes utilise la bonne version.
3. **Vérifie l’état des pods** après mise à jour pour détecter les erreurs.
4. Utilise `rollout status` ou `kubectl logs` pour confirmer que l’application fonctionne correctement.

