package de.industrieschule.vp.core.utilities.helper;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * A fabulous class for handling public and private keys, sweetie!
 * This class provides methods for saving and reading PEM-formatted keys.
 *
 * @author Moritz Hofmann
 */
public class KeyStorage {

    /**
     * Saves a public key to a file.
     *
     * @param publicKey The public key to save.
     * @param filePath The path to the file where the key will be saved.
     * @throws IOException If an I/O error occurs.
     */
    public static void savePublicKey(PublicKey publicKey, Path filePath) throws IOException {
        PemObject pemObject = new PemObject("PUBLIC KEY", publicKey.getEncoded());
        try (PemWriter pemWriter = new JcaPEMWriter(new FileWriter(filePath.toFile()))) {
            pemWriter.writeObject(pemObject);
        }
    }

    /**
     * Saves a public key to a string.
     *
     * @param publicKey The public key to save.
     * @return The PEM-formatted public key as a string.
     * @throws IOException If an I/O error occurs.
     */
    public static String savePublicKeyToString(PublicKey publicKey) throws IOException {
        PemObject pemObject = new PemObject("PUBLIC KEY", publicKey.getEncoded());
        StringWriter sw = new StringWriter();
        try (PemWriter pemWriter = new JcaPEMWriter(sw)) {
            pemWriter.writeObject(pemObject);
        }
        return sw.toString();
    }

    /**
     * Saves a private key to a file.
     *
     * @param privateKey The private key to save.
     * @param filePath The path to the file where the key will be saved.
     * @throws IOException If an I/O error occurs.
     */
    public static void savePrivateKey(PrivateKey privateKey, Path filePath) throws IOException {
        PemObject pemObject = new PemObject("PRIVATE KEY", privateKey.getEncoded());
        try (PemWriter pemWriter = new JcaPEMWriter(new FileWriter(filePath.toFile()))) {
            pemWriter.writeObject(pemObject);
        }
    }

    /**
     * Reads a public key from a file.
     *
     * @param filePath The path to the file containing the public key.
     * @return The public key.
     * @throws IOException If an I/O error occurs.
     * @throws NoSuchAlgorithmException If the algorithm is not supported.
     * @throws InvalidKeySpecException If the key specification is invalid.
     */
    public static PublicKey readPublicKey(Path filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        try (PEMParser pemParser = new PEMParser(new FileReader(filePath.toFile()))) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            Object pemObject = pemParser.readObject();
            if (pemObject instanceof SubjectPublicKeyInfo) {
                return converter.getPublicKey((SubjectPublicKeyInfo) pemObject);
            } else if (pemObject instanceof PEMKeyPair) {
                PEMKeyPair pemKeyPair = (PEMKeyPair) pemObject;
                return converter.getPublicKey(pemKeyPair.getPublicKeyInfo());
            } else {
                throw new InvalidKeySpecException("Invalid PEM file format: " + filePath);
            }
        }
    }

    /**
     * Reads a private key from a file.
     *
     * @param filePath The path to the file containing the private key.
     * @return The private key.
     * @throws IOException If an I/O error occurs.
     * @throws NoSuchAlgorithmException If the algorithm is not supported.
     * @throws InvalidKeySpecException If the key specification is invalid.
     */
    public static PrivateKey readPrivateKey(Path filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        FileReader fileReader = new FileReader(filePath.toFile());
        PEMParser pemParser = new PEMParser(fileReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        Object object = pemParser.readObject();
        if (object instanceof PrivateKeyInfo) {
            PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) object;
            return converter.getPrivateKey(privateKeyInfo);
        } else {
            throw new IllegalArgumentException("Invalid private key format.");
        }
    }
}

