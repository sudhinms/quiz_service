pipeline {
  agent {
    docker {
      image 'sudhinms/jenkins-docker-agent:v1' // ← Replace with your Docker agent image (e.g., Maven + Docker tools)
      args '--user root -v /var/run/docker.sock:/var/run/docker.sock' // ← Necessary to allow Docker usage inside container
    }
  }
  environment {
    // Global environment variables (optional)
  }
  stages {
    stage('Checkout') {
      steps {
        // 1. Provide a git checkout command with your repo parameterized
        git branch: 'main', url: 'https://github.com/sudhinms/quiz_service.git'
        sh 'echo "branch checkout to main"'
      }
    }

    stage('Build and Test') {
      steps {
        sh 'ls -ltr' // → Optional debugging: list workspace contents
        // 2. Navigate to your project directory and build/package
        //   e.g., sh 'cd path/to/project
        sh 'echo "building image..."'
        sh 'mvn clean package'
      }
    }

    stage('Static Code Analysis') {
      environment {
        SONAR_URL = "http://localhost:9000" // ← Replace with your SonarQube URL
      }
      steps {
        withCredentials([string(credentialsId: 'sonarqube', variable: 'SONAR_AUTH_TOKEN')]) { // ← Provide credentials ID(same as the ID in Global credentials section)
        // cd your-app-directory && mvn command for running the sonarqube test
          sh '''
            echo "analyzing code...."
            mvn sonar:sonar \
              -Dsonar.login=$SONAR_AUTH_TOKEN \
              -Dsonar.host.url=${SONAR_URL}
          '''
        }
      }
    }

    stage('Build and Push Docker Image') {
      environment {
        DOCKER_IMAGE = "sudhinms/quiz-app:${BUILD_NUMBER}" // ← Customize image name
        REGISTRY_CREDENTIALS = credentials('docker-registry-cred-id')    // ← Provide Docker registry credentials ID
      }
      steps {
        script {
          sh 'echo "Building docker image..."'
          sh '''
            docker build -t ${DOCKER_IMAGE} .
          '''
          sh 'echo "Image build completed.\n Pushing image to dockerhub..."'
          def dockerImage = docker.image("${DOCKER_IMAGE}")
          docker.withRegistry('https://index.docker.io/v1/', 'docker-registry-cred-id') {
            dockerImage.push()
          }
        }
      }
    }

    // stage('Update Deployment File') {
    //   environment {
    //     GIT_REPO_NAME = "quiz_service"   // ← Replace with your repo
    //     GIT_USER_NAME = "sudhinms"        // ← Replace with your GitHub username
    //   }
    //   steps {
    //     withCredentials([string(credentialsId: 'github', variable: 'GITHUB_TOKEN')]) {
    //       sh '''
    //         git config user.email "sudhin861@gmail.com"
    //         git config user.name "sudhinms"

    //         BUILD_NUMBER=${BUILD_NUMBER}
    //         sed -i "s/replaceImageTag/${BUILD_NUMBER}/g" ./deployment.yml

    //         git add ./deployment.yml
    //         git commit -m "Update image tag to version ${BUILD_NUMBER}"
    //         git push https://${GITHUB_TOKEN}@github.com/${GIT_USER_NAME}/${GIT_REPO_NAME}.git HEAD:main
    //       '''
    //     }
    //   }
    // }
  }

  post {
    success {
      echo "Pipeline succeeded."
    }
    failure {
      echo "Pipeline failed."
    }
    always {
      echo "Pipeline completed."
    }
  }
}

