package com.vallacartelera.app.auth.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vallacartelera.app.auth.dao.IUserDao;
import com.vallacartelera.app.auth.models.Usuario;

@Service
public class IUserServiceImp implements IUserService {

	protected final Log logger = LogFactory.getLog(getClass());
	protected Encoder encoder = Base64.getUrlEncoder().withoutPadding();

	private static final int ITERATIONS = 10000;
	private static final int KEY_LENGTH = 512;

	@Autowired
	private IUserDao userDao;

	@Override
	public Usuario register(Usuario user) {
		SecureRandom random = new SecureRandom();
		Usuario newUser = new Usuario();

		byte bytes[] = new byte[20];
		random.nextBytes(bytes);
		String salt = encoder.encodeToString(bytes);

		String password = user.getPassword();

		String hashPassword = encoder.encodeToString(generateHashedPassword(salt, password));

		/*
		 * TODO: Check if we should save password and salt as Strings or if we should
		 * save it as other thing such as byte[]
		 */
		newUser.setEnabled(true);
		newUser.setUsername(user.getUsername());
		newUser.setPassword(hashPassword);
		newUser.setSalt(salt);

		userDao.save(newUser);
		return newUser;
	}

	/*
	 * TODO: Check if I should return ResponseEntity or if I should leave that part
	 * to the Controller
	 */
	@Override
	public ResponseEntity<?> login(Usuario user) {

		Map<String, Object> result = new HashMap<>();

		Usuario foundUser = userDao.findByUsername(user.getUsername());
		String token = "";
		if (foundUser == null) {
			result.put("error", "Incorrect username or password");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.BAD_REQUEST);
		}
		/*
		 * TODO: Check this part because this looks like we use too many resources to
		 * achieve the comparison of the passwords
		 */
		String salt = foundUser.getSalt();
		String realPassword = foundUser.getPassword();
		String passwordToCheck = encoder.encodeToString(generateHashedPassword(salt, user.getPassword()));
		if (MessageDigest.isEqual(realPassword.getBytes(), passwordToCheck.getBytes())) {
			token = "temporary_token";
			result.put("token", token);
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
		} else {
			result.put("error", "Incorrect username or password");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.BAD_REQUEST);
		}
	}

	private byte[] generateHashedPassword(String salt, String password) {
		SecretKeyFactory secretKeyFactory;
		try {
			secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		} catch (NoSuchAlgorithmException e) {
			logger.error("Error creating user: " + e.getLocalizedMessage());
			return null;
		}
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATIONS, KEY_LENGTH);
		SecretKey key;
		try {
			key = secretKeyFactory.generateSecret(spec);
		} catch (InvalidKeySpecException e) {
			logger.error("Error creating user: " + e.getLocalizedMessage());
			return null;
		}

		return key.getEncoded();
	}
}
