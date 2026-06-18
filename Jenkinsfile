pipeline {
	agent any

	tools {
	    jdk 'jdk-11'
        maven 'maven-3.9.12'
	}
	parameters {
	    string(name: 'jSS7_MAJOR_VERSION_NUMBER', defaultValue: '9.0.0', description: 'The major version for Naikeri jSS7')
	    string(name: 'SCTP_MAJOR_VERSION_NUMBER', defaultValue: '2.1.0', description: 'The major version of Naikeri SCTP for Naikeri jSS7')
	    string(name: 'SCTP_BUILD', defaultValue: '39', description: 'The build number of Naikeri SCTP for Naikeri jSS7 to use for the build')
	    string(name: 'jSS7_PROJECT_PATH',
               defaultValue: 'SS7/Naikeri-jSS7',
               description: 'Jenkins folder path for the upstream Naikeri-jSS7 multibranch pipeline')
	}

	stages {
		stage('Set Version') {
			steps {
				sh "mvn versions:set -DnewVersion=${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}"
				echo "Setting version to ${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER} completed"
			}
		}

		stage("Build") {
            steps {
                script {
                    def NAIKERI_SCTP_VERSION = "${params.SCTP_MAJOR_VERSION_NUMBER}-${params.SCTP_BUILD}"
                    currentBuild.displayName = "#${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}"
                    currentBuild.description = "Naikeri jSS7 build"
                    if (env.BRANCH_NAME == 'master' || env.BRANCH_NAME == 'release') {
                        sh "mvn clean install -Dsctp.version=${NAIKERI_SCTP_VERSION} -Dss7.restcomm.version=${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}"
                    } else {
                        sh "mvn clean install -Dsctp.version=${NAIKERI_SCTP_VERSION} -Dss7.restcomm.version=${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER} -DskipTests"
                    }
                }
                echo "Maven build completed."
            }
        }

		stage("Ant") {
            steps {
                script {
                    def NAIKERI_SCTP_VERSION = "${params.SCTP_MAJOR_VERSION_NUMBER}-${params.SCTP_BUILD}"
                    echo "Starting ant build for version #${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}"
                    if (env.BRANCH_NAME == 'master' || env.BRANCH_NAME == 'release') {
                        withCredentials([usernamePassword(credentialsId: '426e8cfb-a47c-4fd2-96ae-713c541dc3f6',
                                                          usernameVariable: 'ART_USER',
                                                          passwordVariable: 'ART_PASS')]) {
                            withAnt(installation: 'Ant_1.10.15') {
                                dir('release') {
                                    sh "ant -f build.xml -Drelease.version=${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER} -Dsctp.version=${NAIKERI_SCTP_VERSION} -Dartifactory.user=${ART_USER} -Dartifactory.password=${ART_PASS}"
                                }
                            }
                        }
                    } else {
                        withCredentials([usernamePassword(credentialsId: '426e8cfb-a47c-4fd2-96ae-713c541dc3f6',
                                                          usernameVariable: 'ART_USER',
                                                          passwordVariable: 'ART_PASS')]) {
                            withAnt(installation: 'Ant_1.10.15') {
                                dir('release') {
                                    sh "ant -f build.xml -Drelease.version=${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER} -Dsctp.version=${NAIKERI_SCTP_VERSION} -Dartifactory.user=${ART_USER} -Dartifactory.password=${ART_PASS}"
                                }
                            }
                        }
                    }
                }
            }
        }

		stage('Save Artifacts') {
            steps {
                echo "Archiving Naikeri-jSS7-${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}"
                archiveArtifacts artifacts: "release/Naikeri-jSS7-${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}.zip", followSymlinks: false, onlyIfSuccessful: true
            }
        }

        stage('Push Zip to Artifactory') {
            when { anyOf { branch 'master'; branch 'release' } }
            steps {
                withCredentials([usernamePassword(credentialsId: '426e8cfb-a47c-4fd2-96ae-713c541dc3f6',
                                                  usernameVariable: 'ART_USER', passwordVariable: 'ART_PASS')]) {
                    sh """
                        curl -fk -u \${ART_USER}:\${ART_PASS} -X PUT \
                        'https://ec2-56-126-119-82.sa-east-1.compute.amazonaws.com/artifactory/libs-release-local/NAIKERI/jss7/${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}/Naikeri-jSS7-${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}.zip' \
                        -T release/Naikeri-jSS7-${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}.zip
                    """
                }
                echo "Pushed Naikeri-jSS7-${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}.zip to Artifactory"
            }
        }

        stage('Push to Repo') {
            when { anyOf { branch 'master'; branch 'release' } }
		    steps {
		        sh "mkdir -p /var/www/html/NAIKERI/jss7/${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}/"
		        sh "cp release/Naikeri-jSS7-${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}.zip /var/www/html/NAIKERI/jss7/${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}/"
		    }
	    }

	    stage('Push to jFrog') {
	        when { anyOf { branch 'master'; branch 'release' } }
	        steps {
	            sh 'mvn deploy -DskipTests'
	        }
	    }
	}

	post {
		success {
			echo "Successfully build"
		}
        failure {
            script {
                currentBuild.description = "Naikeri jSS7 build - failed"
            }
        }
		always {
			echo "This will be called always. After testing do clean up"
		}
	}
}