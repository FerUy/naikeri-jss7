pipeline {
	agent any

	tools {
	    jdk 'jdk-11'
        maven 'maven-3.9.12'
	}
	parameters {
	    string(name: 'jSS7_MAJOR_VERSION_NUMBER', defaultValue: '9.0.0', description: 'The major version for Naikeri jSS7')
	    string(name: 'SCTP_MAJOR_VERSION_NUMBER', defaultValue: '2.1.0', description: 'The major version of Naikeri SCTP for Naikeri jSS7')
	    string(name: 'SCTP_BUILD', defaultValue: '32', description: 'The build number of Naikeri SCTP for Naikeri jSS7 to use for the build')
	    // booleanParam(name: 'BUILD_ANT', defaultValue: true, description: 'Enable if Binary needs to be generated')
	}

	stages {
		stage('Set Version') {
			steps {
			    script {
                    if (BUILD_NUMBER == "1") {
                        error "Building for the first time"
                    }
                }
				sh "mvn versions:set -DnewVersion=${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}"
				echo "Setting version to ${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER} completed"
			}
		}

		stage("Build") {
			steps {
				script {
                    NAIKERI_SCTP_VERSION = "${params.SCTP_MAJOR_VERSION_NUMBER}-${params.SCTP_BUILD}"
                    currentBuild.displayName = "#${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}"
                    currentBuild.description = "Naikeri jSS7 build"
                }
				echo "Building Naikeri jSS7 version (#${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER})"
				sh "mvn clean install -Dsctp.version=${NAIKERI_SCTP_VERSION} -Dss7.restcomm.version=${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER} -DskipTests"
				echo "Maven build completed."
			}
		}

		stage("Ant") {
            steps {
                script {
                    NAIKERI_SCTP_VERSION = "${params.SCTP_MAJOR_VERSION_NUMBER}-${params.SCTP_BUILD}"
                }
                echo "Starting ant build for version #${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}"
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
		stage('Save Artifacts') {
            steps {
                echo "Archiving Naikeri-jSS7-${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}"
                archiveArtifacts artifacts: "release/Naikeri-jSS7-${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}.zip", followSymlinks: false, onlyIfSuccessful: true
            }
        }
        stage('Push to Repo') {
            when { anyOf { branch 'master'; branch 'release' } }
		    steps {
		        sh "mkdir -p /var/www/html/NAIKERI/jss7/${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}/"
		        sh "cp release/Naikeri-jSS7-${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}.zip /var/www/html/NAIKERI/jss7/${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}/"
		        /*sshagent (credentials: ['4e708f2a-8b37-414f-a6d4-787690b87738']) {
		            sh 'ssh -o StrictHostKeyChecking=no -l fer 127.0.0.1 uname -a'
				    sh "scp release/Naikeri-jSS7-${params.jSS7_MAJOR_VERSION_NUMBER}-${BUILD_NUMBER}.zip fer@127.0.0.1:/var/www/html/NAIKERI/jss7/"
	  	        }*/
		    }
	    }

	    stage('Push to jFrog') {
	        when {anyOf {branch 'master'; branch 'release'}}
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